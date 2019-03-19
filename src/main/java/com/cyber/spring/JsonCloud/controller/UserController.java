package com.cyber.spring.JsonCloud.controller;

import com.cyber.spring.JsonCloud.entity.UserAccount;
import com.cyber.spring.JsonCloud.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    UserInfoRepository userDao;

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<UserAccount> listUsers(){

        return userDao.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserAccount addUser(@RequestBody Map<String,Object> request){

        UserAccount userAccount = new UserAccount();
        userAccount.setLogin( (String)request.get("login") );
        userAccount.setFullName( (String)request.getOrDefault("fullname", "") );

        return userDao.save(userAccount);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserAccount setUserDetails(@PathVariable Long id, @RequestBody Map<String,Object> request){

        Optional<UserAccount> user = userDao.findById(id);

        if (!user.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");

        UserAccount u = user.get();
        u.setLogin((String) request.get("login"));
        u.setFullName((String) request.getOrDefault("fullname", ""));
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
