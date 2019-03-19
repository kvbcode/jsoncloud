package com.cyber.spring.JsonCloud.entity;

import javax.persistence.*;

@Entity
@Table(name="USERS")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(name="LOGIN", length = 128, nullable = false, unique = true)
    String login;

    @Column(name="FULLNAME", nullable = false)
    String fullName = "";

    public UserAccount() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
