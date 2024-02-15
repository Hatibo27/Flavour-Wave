package com.hatib.ckeecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hatib.ckeecommerce.model.OrderItem;
import com.hatib.ckeecommerce.model.Payment;
import com.hatib.ckeecommerce.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	
	public void addOrder(OrderItem item) {
		orderRepository.save(item);
	}
	
	public OrderItem findBypayment(Payment payment){
		
		return orderRepository.findByorder(payment);
	}
	
	public List<OrderItem> findByAll(){
		return orderRepository.findAll();
	}
	
	
	

}
