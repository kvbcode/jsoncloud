package com.cyber.spring.JsonCloud.service;

import com.cyber.spring.JsonCloud.entity.UserAccount;

import java.util.Optional;

public interface UserAccountService {

    UserAccount save(UserAccount user);

    void deleteById(Long id);

    Iterable<UserAccount> findAll();

    Optional<UserAccount> findById(Long id);

}
