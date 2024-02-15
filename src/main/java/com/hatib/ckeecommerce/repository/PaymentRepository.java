package com.hatib.ckeecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hatib.ckeecommerce.model.Payment;
import com.hatib.ckeecommerce.model.User;

public interface PaymentRepository extends JpaRepository<Payment,Integer>{

	public Payment findByorderId(String orderId);
	
	public Payment findAllByusers(User s);
	
	public List<Payment> findByworker(User worker);
	
	public List<Payment> findByusersOrderByIdDesc(User userId);
}
