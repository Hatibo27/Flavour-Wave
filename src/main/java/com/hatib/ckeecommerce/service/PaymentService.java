package com.hatib.ckeecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hatib.ckeecommerce.model.Payment;
import com.hatib.ckeecommerce.model.User;
import com.hatib.ckeecommerce.repository.PaymentRepository;

@Service
public class PaymentService {

	@Autowired
	PaymentRepository paymentRepository;
	
	public void addPayment(Payment payment) {
		paymentRepository.save(payment);
	}
	public Payment findByOrderId(String orderId) {
		return paymentRepository.findByorderId(orderId);
	}
	
	public Payment findAllByusers(User users) {
		return paymentRepository.findAllByusers(users);
	}
	public List<Payment> findByusers(User userId) {
		return paymentRepository.findByusersOrderByIdDesc(userId);
	}
	public Optional<Payment> findByusers(int userId) {
		return paymentRepository.findById(userId);
	}
	
	public Optional<Payment> findById(int id) {
		return paymentRepository.findById(id);
	}
	public List<Payment> findAllByworker(User users) {
		return paymentRepository.findByworker(users);
	}
	
}
