package com.cyber.spring.JsonCloud.controller.admin;

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

@RestController
@RequestMapping(value = "/admin/user")
public class UserAccountController {
    @Autowired
    UserRepository userDao;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @ResponseBody
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<UserAccount> listUsers(){

        return userDao.findAll();
    }

    @ResponseBody
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @PostMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserAccount setUserDetails(@PathVariable Long userId, @RequestBody Map<String,String> request){

        UserAccount u = userDao.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

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
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserAccount getUserDetails(@PathVariable Long userId){

        UserAccount u = userDao.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        return u;
    }

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void delUser(@PathVariable Long id){
        userDao.deleteById(id);
    }

}
