package com.zesty.ecom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zesty.ecom.Model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	boolean existsByName(String name);
	Role getByName(String name);
	
}
