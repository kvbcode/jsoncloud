package com.cyber.spring.JsonCloud.repository;

import com.cyber.spring.JsonCloud.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserAccount, Long> {

    UserAccount findByLogin(String login);

}
