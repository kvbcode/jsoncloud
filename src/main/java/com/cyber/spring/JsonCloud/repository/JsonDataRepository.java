package com.cyber.spring.JsonCloud.repository;

import com.cyber.spring.JsonCloud.entity.JsonDataRecord;
import com.cyber.spring.JsonCloud.entity.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface JsonDataRepository extends CrudRepository<JsonDataRecord, Long> {

    //@Query("SELECT * FROM jsondata WHERE user_id=:userId")
    //Iterable<JsonDataRecord> findByUserId(@Param("userId") Long userId);

    Iterable<JsonDataRecord> findByUserAccount(UserAccount userAccount);

    //@Query("SELECT * FROM jsondata WHERE user_id=:userId AND app_id=:appId")
    Iterable<JsonDataRecord> findByUserAccountAndAppId(UserAccount userAccount, Integer appId);

    //@Query("SELECT * FROM jsondata WHERE user_id=:userId AND app_id=:appId AND data_type=:dataType")
    Iterable<JsonDataRecord> findByUserAccountAndAppIdAndDataType(UserAccount userAccount, Integer appId, Integer dataType);

}
