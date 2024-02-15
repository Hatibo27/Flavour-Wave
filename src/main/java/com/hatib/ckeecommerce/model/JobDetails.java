package com.hatib.ckeecommerce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="jobDetails")
public class JobDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "type_of_job")
	private String typeOfJob;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "zipcode")
	private String zipCode;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "vehicle_no")
	private String vehicleNo;
	
	@Column(name = "image_name")
	private String imageName;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User users;
	
	@Column(name="date")
	private String date;
	
	@Column(name="status")
	private String status;
	
	@Column(name="quality")
	private String quality;
	
	@Column(name="remark")
	private String remark;
	
	

	public JobDetails() {

	}



	public JobDetails(int id,String status,String remark,String quality, String firstName,String date, String lastName, String typeOfJob, String address, String zipCode,
			String phone, String vehicleNo, String imageName, User users) {
		
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.typeOfJob = typeOfJob;
		this.address = address;
		this.zipCode = zipCode;
		this.phone = phone;
		this.vehicleNo = vehicleNo;
		this.imageName = imageName;
		this.users = users;
		this.date = date;
		this.status=status;
		this.quality=quality;
		this.remark = remark;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	



	public String getQuality() {
		return quality;
	}



	public void setQuality(String quality) {
		this.quality = quality;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getTypeOfJob() {
		return typeOfJob;
	}



	public void setTypeOfJob(String typeOfJob) {
		this.typeOfJob = typeOfJob;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getZipCode() {
		return zipCode;
	}



	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public String getVehicleNo() {
		return vehicleNo;
	}



	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}



	public String getImageName() {
		return imageName;
	}



	public void setImageName(String imageName) {
		this.imageName = imageName;
	}



	public User getUsers() {
		return users;
	}



	public void setUsers(User users) {
		this.users = users;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
	
}
