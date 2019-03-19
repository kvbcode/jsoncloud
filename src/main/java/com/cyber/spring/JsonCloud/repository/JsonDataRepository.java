package com.cyber.spring.JsonCloud.repository;

import com.cyber.spring.JsonCloud.entity.DataRecord;
import com.cyber.spring.JsonCloud.entity.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface JsonDataRepository extends CrudRepository<DataRecord, Long> {

    Iterable<DataRecord> findByUserAccount(@Param("userAccount") UserAccount userAccount);

    @Query("from DataRecord e where e.userAccount = :userAccount and e.appId = :appId")
    Iterable<DataRecord> findByAppId(@Param("userAccount") UserAccount userAccount, @Param("appId") Integer appId);

    @Query("from DataRecord e where e.userAccount = :userAccount and e.appId = :appId and dataType = :dataType")
    Iterable<DataRecord> findByDataType(@Param("userAccount") UserAccount userAccount, @Param("appId") Integer appId, @Param("dataType") Integer dataType);

}
