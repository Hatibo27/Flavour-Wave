package com.hatib.ckeecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hatib.ckeecommerce.dto.ProductDTO;
import com.hatib.ckeecommerce.model.Product;
import com.hatib.ckeecommerce.repository.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	public void addProducts(Product product) {
		productRepository.save(product);
	}
	
	public void deleteProducts(Long id) {
		productRepository.deleteById(id);
	}
	public Optional<Product> getProductById(Long id){
		
		return productRepository.findById(id);
	}
	
	public List<Product> getProductByCategoryId(Long id){
		return productRepository.findByCategoryId(id);
	}
	

}
