package com.hatib.ckeecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hatib.ckeecommerce.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{

	Optional<User> findUserByEmail(String email);
	
    User  findUserrByEmail(String email);
    
    User findByEmail(String email);
		
	

	
}
