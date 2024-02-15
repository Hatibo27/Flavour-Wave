package com.hatib.ckeecommerce.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;





@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable= true)
	private String firstName;

	@Column(nullable=true)
	private String lastName;
	
	
	@Column(nullable=true,unique=true)
	private String email;
	
	private String password;
	
	


 
	
	@ManyToMany(cascade = CascadeType.MERGE,fetch= FetchType.EAGER)
	@JoinTable(name="user_role",joinColumns = {@JoinColumn(name="USER_ID",referencedColumnName = "ID")},
			inverseJoinColumns= {@JoinColumn(name="ROLE_ID",referencedColumnName = "ID")})
	private List<Role> roles;
	
	
	
	@OneToMany(mappedBy ="users", cascade =CascadeType.ALL)
	private List<Payment> payment;
	
	@OneToMany(mappedBy ="worker", cascade =CascadeType.ALL)
	private List<Payment> paymentWorker;
	
	@OneToMany(mappedBy ="users", cascade =CascadeType.ALL)
	private List<JobDetails> jobDetals;
	
	

	public User() {
	
	}

	public User(User user) {
		this.id = user.id;
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.roles = user.getRoles();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}


	public List<Payment> getPayment() {
		return payment;
	}

	public void setPayment(List<Payment> payment) {
		this.payment = payment;
	}

	public List<Payment> getPaymentWorker() {
		return paymentWorker;
	}

	public void setPaymentWorker(List<Payment> paymentWorker) {
		this.paymentWorker = paymentWorker;
	}

	public List<JobDetails> getJobDetals() {
		return jobDetals;
	}

	public void setJobDetals(List<JobDetails> jobDetals) {
		this.jobDetals = jobDetals;
	}
	
	

	
	
	
	
	
	
	
	
}
