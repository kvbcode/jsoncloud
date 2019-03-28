package com.cyber.spring.JsonCloud.repository;

import com.cyber.spring.JsonCloud.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserAccount, Long> {

    Optional<UserAccount> findByLogin(String login);

}
