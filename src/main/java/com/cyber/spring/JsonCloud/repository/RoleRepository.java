package com.cyber.spring.JsonCloud.repository;

import com.cyber.spring.JsonCloud.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String login);

}
