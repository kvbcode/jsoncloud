package com.cyber.spring.JsonCloud.controller;

import com.cyber.spring.JsonCloud.entity.Role;
import com.cyber.spring.JsonCloud.entity.UserAccount;
import com.cyber.spring.JsonCloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserRepository userDao;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<UserAccount> listUsers(){

        return userDao.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserAccount addUser(@RequestBody Map<String,String> request){

        UserAccount userAccount = new UserAccount();
        userAccount.setLogin( request.get("login") );
        userAccount.setFullName( request.getOrDefault("fullname", "") );
        userAccount.setPassword( passwordEncoder.encode( request.getOrDefault("password", "") ) );
        userAccount.setActiveStatus(1);
        userAccount.setRoles( Arrays.asList(new Role("ROLE_USER") ) );

        return userDao.save(userAccount);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserAccount setUserDetails(@PathVariable Long id, @RequestBody Map<String,String> request){

        Optional<UserAccount> user = userDao.findById(id);

        if (!user.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");

        UserAccount u = user.get();
        u.setLogin( request.getOrDefault("login", u.getLogin()));
        u.setFullName( request.getOrDefault("fullname", u.getFullName()));

        String rawPassword = request.getOrDefault("password", "");
        if (!rawPassword.isEmpty()) {
            u.setPassword( passwordEncoder.encode( rawPassword ) );
        }

        userDao.save(u);
        return u;
    }


    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserAccount getUserDetails(@PathVariable("id") Long id){
        Optional<UserAccount> user = userDao.findById(id);

        if (!user.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");

        return user.get();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void delUser(@PathVariable("id") Long id){
        userDao.deleteById(id);
    }

}
