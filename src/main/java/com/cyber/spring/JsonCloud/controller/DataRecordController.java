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

import javax.servlet.http.HttpServletRequest;

/**
 * Работа пользователей со своими данными.
 * Аккаунт авторизованного пользователя определяется из запроса.
 */

@RestController
@RequestMapping(value = "/data")
public class DataRecordController {
    Logger log = LoggerFactory.getLogger(DataRecordController.class);

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
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<DataRecord> listRecordsByUser(HttpServletRequest request){

        UserAccount u = userDao.findByLogin( request.getUserPrincipal().getName() );

        return jsonDataDao.findByUserAccount(u);
    }

    @ResponseBody
    @GetMapping(value = "/app/{appId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<DataRecord> listRecordsByApp(HttpServletRequest request, @PathVariable Integer appId){

        UserAccount u = userDao.findByLogin( request.getUserPrincipal().getName() );

        return jsonDataDao.findByAppId(u, appId);
    }

    @ResponseBody
    @GetMapping(value = "/app/{appId}/{dataType}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<DataRecord> listRecordsByType(HttpServletRequest request, @PathVariable Integer appId, @PathVariable Integer dataType){

        UserAccount u = userDao.findByLogin( request.getUserPrincipal().getName() );

        return jsonDataDao.findByDataType(u, appId, dataType);
    }


    @ResponseBody
    @PostMapping(value = "/app/{appId}/{dataType}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String add(HttpServletRequest request, @PathVariable Integer appId, @PathVariable Integer dataType, @RequestBody String jsonDataStr){

        UserAccount u = userDao.findByLogin( request.getUserPrincipal().getName() );

        DataRecord e = new DataRecord();
        e.setUserAccount( u );
        e.setAppId( appId );
        e.setDataType( dataType );
        e.setJsonData( jsonDataStr );

        jsonDataDao.save( e );
        return jsonResponse(e.getId());
    }

    @ResponseBody
    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String set(HttpServletRequest request, @PathVariable Long id, @RequestBody String jsonDataStr){

        DataRecord r = jsonDataDao.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));

        UserAccount principal = userDao.findByLogin( request.getUserPrincipal().getName() );
        UserAccount entityUser = userDao.findById(r.getUserId()).get();

        if ( !principal.equals( entityUser )) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user mismatch");

        r.setJsonData(jsonDataStr);

        jsonDataDao.save( r );
        return jsonResponse(r.getId());
    }


    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String get(HttpServletRequest request, @PathVariable("id") Long id){

        DataRecord r = jsonDataDao.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));

        UserAccount principal = userDao.findByLogin( request.getUserPrincipal().getName() );
        UserAccount entityUser = userDao.findById(r.getUserId()).get();

        if ( !principal.equals( entityUser ) ) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user mismatch");

        return r.getJsonData();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void del(HttpServletRequest request, @PathVariable("id") Long id){

        DataRecord r = jsonDataDao.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));

        UserAccount principal = userDao.findByLogin( request.getUserPrincipal().getName() );
        UserAccount entityUser = userDao.findById(r.getUserId()).get();

        if ( !principal.equals( entityUser ) ) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user mismatch");

        jsonDataDao.delete( r );
    }

}
