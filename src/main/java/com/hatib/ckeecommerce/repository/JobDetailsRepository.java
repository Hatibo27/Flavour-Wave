package com.hatib.ckeecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hatib.ckeecommerce.model.JobDetails;
import com.hatib.ckeecommerce.model.User;

public interface JobDetailsRepository  extends JpaRepository<JobDetails,Integer>{

	public List<JobDetails> findByusersOrderByIdDesc(User userId);

	public List<JobDetails> findAllByOrderByIdDesc();
}
