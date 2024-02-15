package com.hatib.ckeecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hatib.ckeecommerce.model.OrderItem;
import com.hatib.ckeecommerce.model.Payment;



public interface OrderRepository extends JpaRepository<OrderItem,Integer>{

	public OrderItem findByorder(Payment payment);
	
	

	
	
}
