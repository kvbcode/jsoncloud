package com.cyber.spring.JsonCloud.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "data_records")
public class DataRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    UserAccount userAccount;

    @Column(name = "app_id")
    @JsonProperty("app_id")
    Integer appId = 0;

    @UpdateTimestamp
    @Column(nullable = false)
    @JsonProperty("modified")
    Date modified = new Date();

    @Column(name = "data_type")
    @JsonProperty("data_type")
    Integer dataType = 0;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("json_data")
    String jsonData;

    public DataRecord() {
    }

    @JsonGetter("user_id")
    public Long getUserId(){
        return userAccount.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
