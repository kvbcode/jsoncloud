package com.cyber.spring.JsonCloud.controller;

import com.cyber.spring.JsonCloud.entity.JsonDataRecord;
import com.cyber.spring.JsonCloud.entity.UserAccount;
import com.cyber.spring.JsonCloud.repository.JsonDataRepository;
import com.cyber.spring.JsonCloud.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/data")
public class JsonDataRecordController {
    Logger log = LoggerFactory.getLogger(JsonDataRecordController.class);

    @Autowired
    UserInfoRepository userDao;

    @Autowired
    JsonDataRepository jsonDataDao;

    @ResponseBody
    @GetMapping(value = "/{userId}/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<JsonDataRecord> listRecords(@PathVariable Long userId){

        Optional<UserAccount> optUser = userDao.findById(userId);

        if (!optUser.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");

        UserAccount u = optUser.get();

        return jsonDataDao.findByUserAccount(u);
    }

    @ResponseBody
    @GetMapping(value = "/{userId}/{appId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<JsonDataRecord> listRecordsByApp(@PathVariable Long userId, @PathVariable Integer appId){

        Optional<UserAccount> optUser = userDao.findById(userId);

        if (!optUser.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");

        UserAccount u = optUser.get();

        return jsonDataDao.findByAppId(u, appId);
    }

    @ResponseBody
    @GetMapping(value = "/{userId}/{appId}/{dataType}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<JsonDataRecord> listRecordsByType(@PathVariable Long userId, @PathVariable Integer appId, @PathVariable Integer dataType){

        Optional<UserAccount> optUser = userDao.findById(userId);

        if (!optUser.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");

        UserAccount u = optUser.get();

        return jsonDataDao.findByDataType(u, appId, dataType);
    }


    @ResponseBody
    @PostMapping(value = "/{userId}/{appId}/{dataType}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonDataRecord add(@PathVariable Long userId, @PathVariable Integer appId, @PathVariable Integer dataType, @RequestBody String jsonDataStr){
        log.info("Add user_id=" + userId);
        Optional<UserAccount> optUser = userDao.findById(userId);

        if (!optUser.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");

        JsonDataRecord e = new JsonDataRecord();
        e.setUserAccount( optUser.get() );
        e.setAppId( appId );
        e.setDataType( dataType );
        e.setJsonData( jsonDataStr );

        return jsonDataDao.save( e );
    }

    @ResponseBody
    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonDataRecord set(@PathVariable Long id, @RequestBody String jsonDataStr){

        Optional<JsonDataRecord> optData = jsonDataDao.findById(id);

        if (!optData.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found");

        JsonDataRecord e = optData.get();

        e.setJsonData(jsonDataStr);

        return jsonDataDao.save( e );
    }


    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonDataRecord get(@PathVariable("id") Long id){
        Optional<JsonDataRecord> optData = jsonDataDao.findById(id);

        if (!optData.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found");

        return optData.get();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void del(@PathVariable("id") Long id){
        jsonDataDao.deleteById( id );
    }

}
