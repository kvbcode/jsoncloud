package com.cyber.spring.JsonCloud;

import com.cyber.spring.JsonCloud.entity.Role;
import com.cyber.spring.JsonCloud.entity.UserAccount;
import com.cyber.spring.JsonCloud.repository.RoleRepository;
import com.cyber.spring.JsonCloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
public class JsonCloudApplication {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(JsonCloudApplication.class, args);
	}


	@PostConstruct
	public void init(){
		UserAccount admin = userRepository.findByLogin("admin").orElseGet(() -> {
			UserAccount acc = new UserAccount();
			acc.setLogin("admin");
			acc.setFullName("Admin");
			acc.setPassword("admin");
			acc.setRoles(Arrays.asList(new Role("ROLE_USER"), new Role("ROLE_ADMIN")));
			acc.setStatus(1);

			return userRepository.save(acc);
		});

	}

}
