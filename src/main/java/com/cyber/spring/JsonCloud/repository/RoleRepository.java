package com.cyber.spring.JsonCloud.repository;

import com.cyber.spring.JsonCloud.entity.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String login);

}
