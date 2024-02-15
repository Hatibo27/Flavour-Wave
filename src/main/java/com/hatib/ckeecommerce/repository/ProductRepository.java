package com.hatib.ckeecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hatib.ckeecommerce.dto.ProductDTO;
import com.hatib.ckeecommerce.model.Product;

public interface ProductRepository extends JpaRepository<Product,Long>{

	 List<Product> findByCategoryId(Long id);
}
