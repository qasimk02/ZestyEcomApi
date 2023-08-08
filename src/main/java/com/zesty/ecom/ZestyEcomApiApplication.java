package com.zesty.ecom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.zesty.ecom.Model.Role;
import com.zesty.ecom.Repository.RoleRepository;

@SpringBootApplication
public class ZestyEcomApiApplication implements CommandLineRunner{

	@Autowired
	private RoleRepository roleRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(ZestyEcomApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role user = new Role();
		user.setId(1);
		user.setName("ROLE_USER");
		
		Role admin = new Role();
		admin.setId(2);
		admin.setName("ROLE_ADMIN");
		
		List<Role> roles = List.of(user,admin);
		roleRepository.saveAll(roles);
		
	}
	
}
