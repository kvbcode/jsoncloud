package com.cyber.spring.JsonCloud.repository;

import com.cyber.spring.JsonCloud.entity.JsonDataRecord;
import com.cyber.spring.JsonCloud.entity.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface JsonDataRepository extends CrudRepository<JsonDataRecord, Long> {

    Iterable<JsonDataRecord> findByUserAccount(@Param("userAccount") UserAccount userAccount);

    @Query("from JsonDataRecord e where e.userAccount = :userAccount and e.appId = :appId")
    Iterable<JsonDataRecord> findByAppId(@Param("userAccount") UserAccount userAccount, @Param("appId") Integer appId);

    @Query("from JsonDataRecord e where e.userAccount = :userAccount and e.appId = :appId and dataType = :dataType")
    Iterable<JsonDataRecord> findByDataType(@Param("userAccount") UserAccount userAccount, @Param("appId") Integer appId, @Param("dataType") Integer dataType);

}
