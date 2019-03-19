package com.cyber.spring.JsonCloud.controller;

import com.cyber.spring.JsonCloud.entity.DataRecord;
import com.cyber.spring.JsonCloud.entity.UserAccount;
import com.cyber.spring.JsonCloud.repository.JsonDataRepository;
import com.cyber.spring.JsonCloud.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping(value = "/data")
public class JsonDataRecordController {
    Logger log = LoggerFactory.getLogger(JsonDataRecordController.class);

    @Autowired
    UserRepository userDao;

    @Autowired
    JsonDataRepository jsonDataDao;

    @ResponseBody
    @GetMapping(value = "/u/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<DataRecord> listRecordsbyUser(@PathVariable Long userId, Principal principal){

        Optional<UserAccount> optUser = userDao.findById(userId);

        if (!optUser.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");

        UserAccount u = optUser.get();

        if ( !principal.getName().equals( u.getLogin() ) ) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user mismatch");

        return jsonDataDao.findByUserAccount(u);
    }

    @ResponseBody
    @GetMapping(value = "/{userId}/{appId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<DataRecord> listRecordsByApp(@PathVariable Long userId, @PathVariable Integer appId, Principal principal){

        Optional<UserAccount> optUser = userDao.findById(userId);

        if (!optUser.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");

        UserAccount u = optUser.get();

        if ( !principal.getName().equals( u.getLogin() ) ) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user mismatch");

        return jsonDataDao.findByAppId(u, appId);
    }

    @ResponseBody
    @GetMapping(value = "/{userId}/{appId}/{dataType}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<DataRecord> listRecordsByType(@PathVariable Long userId, @PathVariable Integer appId, @PathVariable Integer dataType, Principal principal){

        Optional<UserAccount> optUser = userDao.findById(userId);

        if (!optUser.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");

        UserAccount u = optUser.get();

        if ( !principal.getName().equals( u.getLogin() ) ) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user mismatch");

        return jsonDataDao.findByDataType(u, appId, dataType);
    }


    @ResponseBody
    @PostMapping(value = "/{userId}/{appId}/{dataType}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DataRecord add(@PathVariable Long userId, @PathVariable Integer appId, @PathVariable Integer dataType, @RequestBody String jsonDataStr, Principal principal){
        log.info("Add user_id=" + userId);
        Optional<UserAccount> optUser = userDao.findById(userId);

        if (!optUser.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");

        UserAccount u = optUser.get();

        if ( !principal.getName().equals( u.getLogin() ) ) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user mismatch");

        DataRecord e = new DataRecord();
        e.setUserAccount( u );
        e.setAppId( appId );
        e.setDataType( dataType );
        e.setJsonData( jsonDataStr );

        return jsonDataDao.save( e );
    }

    @ResponseBody
    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DataRecord set(@PathVariable Long id, @RequestBody String jsonDataStr, Principal principal){

        Optional<DataRecord> optData = jsonDataDao.findById(id);

        if (!optData.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found");

        DataRecord e = optData.get();

        UserAccount u = userDao.findById(e.getUserId()).get();

        if ( !principal.getName().equals( u.getLogin() ) ) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user mismatch");

        e.setJsonData(jsonDataStr);

        return jsonDataDao.save( e );
    }


    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DataRecord get(@PathVariable("id") Long id, Principal principal){
        Optional<DataRecord> optData = jsonDataDao.findById(id);

        if (!optData.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found");

        DataRecord e = optData.get();

        UserAccount u = userDao.findById(e.getUserId()).get();

        if ( !principal.getName().equals( u.getLogin() ) ) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user mismatch");

        return optData.get();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void del(@PathVariable("id") Long id, Principal principal){

        Optional<DataRecord> optData = jsonDataDao.findById(id);

        if (!optData.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found");

        DataRecord e = optData.get();

        UserAccount u = userDao.findById(e.getUserId()).get();

        if ( !principal.getName().equals( u.getLogin() ) ) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user mismatch");

        jsonDataDao.delete( e );
    }

}
