package com.hatib.ckeecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hatib.ckeecommerce.model.JobDetails;
import com.hatib.ckeecommerce.model.User;
import com.hatib.ckeecommerce.repository.JobDetailsRepository;

@Service
public class JobDetailsService {
	
	@Autowired
	JobDetailsRepository jobDetailsRepository;
	
	public JobDetails addJobDetails(JobDetails jobDetails) {
		
		return this.jobDetailsRepository.save(jobDetails);
	}

	public List<JobDetails> findByusers(User userId) {
		return jobDetailsRepository.findByusersOrderByIdDesc(userId);
	}
	
	public List<JobDetails> findAll(){
		return jobDetailsRepository.findAllByOrderByIdDesc();
	}
	
	public Optional<JobDetails> findById(int id) {
		return jobDetailsRepository.findById(id);
	}
}
