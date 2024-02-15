package com.hatib.ckeecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hatib.ckeecommerce.model.Category;
import com.hatib.ckeecommerce.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<Category> getAllCategory(){
		return categoryRepository.findAll();
	}
	
	public void addCategory(Category category) {
		categoryRepository.save(category);
		
	}
	
	public void deleteCategory(Long id) {
		categoryRepository.deleteById(id);;
	}
	
	public Optional<Category> getCategoryById(Long i) {
		return categoryRepository.findById(i);
	}
	
}
