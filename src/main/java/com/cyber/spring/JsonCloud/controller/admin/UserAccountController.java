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

    private String jsonResponse(Long id){
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":").append(id);
        sb.append("}");
        return sb.toString();
    }

    @ResponseBody
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<UserAccount> listUsers(){

        return userDao.findAll();
    }

    @ResponseBody
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String addUser(@RequestBody UserAccount userAccount){

        userAccount.setPassword( passwordEncoder.encode( userAccount.getPassword() ) );
        userDao.save(userAccount);
        return jsonResponse(userAccount.getId());
    }

    @ResponseBody
    @PostMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String setUserDetails(@PathVariable Long userId, @RequestBody UserAccount userRequestData){

        UserAccount u = userDao.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        u.setLogin( userRequestData.getLogin() );
        u.setFullName( userRequestData.getFullName() );
        u.setStatus( userRequestData.getStatus() );
        u.setRoles( userRequestData.getRoles() );

        String password = userRequestData.getPassword();
        if (password!=null && !password.isEmpty()) {
            u.setPassword( passwordEncoder.encode( password ) );
        }

        userDao.save(u);
        return jsonResponse(u.getId());
    }


    @ResponseBody
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserAccount getUserDetails(@PathVariable Long userId){

        UserAccount u = userDao.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        return u;
    }

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void delUser(@PathVariable Long userId){
        userDao.deleteById(userId);
    }

}
