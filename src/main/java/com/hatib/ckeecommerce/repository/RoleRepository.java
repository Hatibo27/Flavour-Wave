package com.hatib.ckeecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hatib.ckeecommerce.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

	Role  findRoleById(int id);
	
	
}
