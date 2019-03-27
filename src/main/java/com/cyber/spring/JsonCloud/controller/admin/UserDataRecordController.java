package com.cyber.spring.JsonCloud.controller.admin;

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

import java.util.HashMap;
import java.util.Map;


/**
 * Работа администраторов с данными пользователей.
 */

@RestController
@RequestMapping(value = "/admin/user")
public class UserDataRecordController {
    Logger log = LoggerFactory.getLogger(UserDataRecordController.class);

    @Autowired
    UserRepository userDao;

    @Autowired
    JsonDataRepository jsonDataDao;

    private String jsonResponse(Long id){
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":").append(id);
        sb.append("}");
        return sb.toString();
    }

    @ResponseBody
    @GetMapping(value = "/{userId}/data", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<DataRecord> listRecordsbyUser(@PathVariable Long userId){

        UserAccount u = userDao.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        return jsonDataDao.findByUserAccount(u);
    }

    @ResponseBody
    @GetMapping(value = "/{userId}/data/{appId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<DataRecord> listRecordsByApp(@PathVariable Long userId, @PathVariable Integer appId){

        UserAccount u = userDao.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        return jsonDataDao.findByAppId(u, appId);
    }

    @ResponseBody
    @GetMapping(value = "/{userId}/data/{appId}/{dataType}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<DataRecord> listRecordsByType(@PathVariable Long userId, @PathVariable Integer appId, @PathVariable Integer dataType){

        UserAccount u = userDao.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        return jsonDataDao.findByDataType(u, appId, dataType);
    }


    @ResponseBody
    @PostMapping(value = "/{userId}/data/{appId}/{dataType}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String add(@PathVariable Long userId, @PathVariable Integer appId, @PathVariable Integer dataType, @RequestBody String rawDataStr){

        UserAccount u = userDao.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        DataRecord r = new DataRecord();
        r.setUserAccount( u );
        r.setAppId( appId );
        r.setDataType( dataType );
        r.setJsonData( rawDataStr );
        jsonDataDao.save( r );

        return jsonResponse(r.getId());
    }

    @ResponseBody
    @PostMapping(value = "/data/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String set(@PathVariable Long id, @RequestBody String rawDataStr){

        DataRecord r = jsonDataDao.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));
        r.setJsonData(rawDataStr);
        jsonDataDao.save( r );

        return jsonResponse(r.getId());
    }


    @ResponseBody
    @GetMapping(value = "/data/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DataRecord get(@PathVariable("id") Long id){

        DataRecord r = jsonDataDao.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));

        return r;
    }

    @DeleteMapping(value = "/data/{id}")
    public void del(@PathVariable("id") Long id){

        DataRecord r = jsonDataDao.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));

        jsonDataDao.delete( r );
    }

}
