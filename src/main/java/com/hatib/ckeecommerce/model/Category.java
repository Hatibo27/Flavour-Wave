package com.hatib.ckeecommerce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="category")
public class Category {

	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	@Column(name="category_id")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	
	

	public Category() {
		
	}
	

	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
