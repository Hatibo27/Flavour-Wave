package com.hatib.ckeecommerce.model;


import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "order_test")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "amount")
	private int amount;

	@Column(name = "date")
	private String date;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "country")
	private String country;

	@Column(name = "address_1")
	private String address1;

	@Column(name = "address_2")
	private String address2;

	@Column(name = "zipcode")
	private Long zipCode;

	@Column(name = "town")
	private String town;

	@Column(name = "phone")
	private String phone;

	@Column(name = "email")
	private String email;

	@Column(name = "addinfo")
	private String addInfo;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User users;

	@Column(name = "order_id")
	private String orderId;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItem;

	@Column(name = "status")
	private String status;

	@Column(name = "payment_id")
	private String paymentId;
	
	@Column(name="order_status_worker")
	private String orderStatus;
	
	@Column(name="order_accept_time")
	private String orderAcceptTime;
	
	@ManyToOne
	@JoinColumn(name = "user_id_worker")
	private User worker;
	
	@Column(name="order_recived_time")
	private String orderRecivedTime;
	
	
	@Column(name="star")
	private String star;

	public Payment() {
	}

	public Payment(int id, String paymentId, int amount, String date, String status, String firstName, String lastName,
			String country, String address1, String address2, Long zipCode, String orderId, String town, String phone,
			String email, String addInfo, List<User> users,String orderStatus, String orderAcceptTime,User worker,String star,String orderRecivedTime) {

		this.id = id;
		this.amount = amount;
		this.date = date;
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
		this.address1 = address1;
		this.address2 = address2;
		this.zipCode = zipCode;
		this.town = town;
		this.phone = phone;
		this.email = email;
		this.addInfo = addInfo;
		this.orderId = orderId;
		this.status = status;
		this.paymentId = paymentId;
		this.orderStatus=status;
		this.orderAcceptTime = orderAcceptTime;
		this.worker=worker;
		this.orderRecivedTime=orderRecivedTime;
		this.star=star;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public Long getZipCode() {
		return zipCode;
	}

	public void setZipCode(Long zipCode) {
		this.zipCode = zipCode;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddInfo() {
		return addInfo;
	}

	public void setAddInfo(String addInfo) {
		this.addInfo = addInfo;
	}

	public User getUsers() {
		return users;
	}

	public void setUsers(User users) {
		this.users = users;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<OrderItem> getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(List<OrderItem> orderItem) {
		this.orderItem = orderItem;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderAcceptTime() {
		return orderAcceptTime;
	}

	public void setOrderAcceptTime(String orderAcceptTime) {
		this.orderAcceptTime = orderAcceptTime;
	}

	public User getWorker() {
		return worker;
	}

	public void setWorker(User worker) {
		this.worker = worker;
	}

	public String getOrderRecivedTime() {
		return orderRecivedTime;
	}

	public void setOrderRecivedTime(String orderRecivedTime) {
		this.orderRecivedTime = orderRecivedTime;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}



	
}
