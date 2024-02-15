package com.hatib.ckeecommerce.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hatib.ckeecommerce.Global.GlobalData;
import com.hatib.ckeecommerce.model.CustomUserDetail;
import com.hatib.ckeecommerce.model.OrderItem;
import com.hatib.ckeecommerce.model.Payment;
import com.hatib.ckeecommerce.model.Product;
import com.hatib.ckeecommerce.model.Role;
import com.hatib.ckeecommerce.model.User;
import com.hatib.ckeecommerce.repository.OrderRepository;
import com.hatib.ckeecommerce.repository.UserRepository;
import com.hatib.ckeecommerce.service.OrderService;
import com.hatib.ckeecommerce.service.PaymentService;
import com.hatib.ckeecommerce.service.ProductService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@Controller
public class CartController {

	@Autowired
	ProductService productService;

	@Autowired
	PaymentService paymentService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderRepository orderRepository;

	@GetMapping("/addToCart/{id}")
	public String addToCart(@PathVariable Long id) {
		GlobalData.cart.add(productService.getProductById(id).get());
		return "redirect:/shop";
	}

	@GetMapping("/cart")
	public String cartGet(Model model,HttpSession session) {
		model.addAttribute("cartCount", GlobalData.cart.size());
		model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
		model.addAttribute("cart", GlobalData.cart);
		
		if(!GlobalData.cart.isEmpty()) {
			session.removeAttribute("message");
		}
		
		return "cart";
	}

	@GetMapping("/cart/removeItem/{index}")
	public String cartItemRemove(@PathVariable int index) {
		GlobalData.cart.remove(index);
		return "redirect:/cart";
	}

	@GetMapping("/checkout")
	public String checkout(Model model,HttpSession session) {

		model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
		
		if(GlobalData.cart.isEmpty()) {
			session.setAttribute("message", "cart is empty !!");
			return "redirect:/cart";
		}else {
			return "checkout";
		}

//		model.addAttribute("user",);
		
	}

	@SuppressWarnings("unused")
	@PostMapping("/checkout/createOrder")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data, Payment payment, Authentication authentication,
			Model model) throws Exception {
		System.out.println("Hey order function ex..");
		int amount = Integer.parseInt(data.get("amount").toString());

		String firstName = data.get("firstName").toString();
		String lastName = data.get("lastName").toString();
		String address1 = data.get("address1").toString();
		String address2 = data.get("address2").toString();
		String zipcode = data.get("zipcode").toString();
		String town = data.get("town").toString();
		String phone = data.get("phone").toString();
		String email = data.get("email").toString();
		String addInfo = data.get("addInfo").toString();

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		var client = new RazorpayClient("rzp_test_xxOZHjjc7WUrc8", "ciQdvWtTahAdBC2yieY5dTYt");
		JSONObject options = new JSONObject();
		options.put("amount", amount * 100);
		options.put("currency", "INR");
		options.put("receipt", "txn_123456");
		Order order = client.orders.create(options);
		System.out.println(order);

		// ifyou want you can save this

		payment.setFirstName(firstName);
		payment.setLastName(lastName);
		payment.setAddress1(address1);
		payment.setAddress2(address2);
		payment.setCountry("INDIA");

		payment.setDate(dtf.format(now));
		if (zipcode == "") {
			int zipcodemain = 0;
			long z = zipcodemain;
			payment.setZipCode(z);
		} else {
			long z = Integer.parseInt(zipcode);

			payment.setZipCode(z);
		}

		payment.setTown(town);

		payment.setPhone(phone);
		payment.setAmount(amount);

		payment.setEmail(email);
		payment.setAddInfo(addInfo);
		payment.setStatus("created");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if (user.getId() == null) {

		}
		try {
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			payment.setUsers(user);
		} catch (Exception e) {
			OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
			String emailUser = token.getPrincipal().getAttributes().get("email").toString();
			if (userRepository.findUserByEmail(emailUser).isPresent()) {
				Optional<User> userData = userRepository.findUserByEmail(emailUser);

				userData.ifPresent(user1 -> {
					payment.setUsers(user1);
				});

//				List<User> hat =userData.stream().collect(Collectors.toList());

			}
		}

		payment.setOrderId(order.get("id"));

		try {
			paymentService.addPayment(payment);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println(data);
		List<?> mlist = GlobalData.cart;

		int i = 12;
		long l = i;

		for (Object r : mlist) {
			Product product = new Product();
			OrderItem item = new OrderItem();
			product = (Product) r;
			item.setProduct(product);
			item.setOrder(payment);
			orderService.addOrder(item);
		}

		return order.toString();
	}

	@PostMapping("/checkout/successOrder")
	public ResponseEntity<?> successOrder(@RequestBody Map<String, Object> data) {

		Payment payment = this.paymentService.findByOrderId(data.get("order_id").toString());

		payment.setOrderId(data.get("order_id").toString());
		payment.setPaymentId(data.get("payment_id").toString());
		payment.setStatus(data.get("status").toString());
		this.paymentService.addPayment(payment);
		GlobalData.cart.clear();
		return ResponseEntity.ok(Map.of("msg", "updated"));
	}
	
	@GetMapping("/orderdata")
	public String getOrderData(Model model,Authentication authentication) {
		
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		try {
			User user = new User();
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			List<Payment> payment = paymentService.findByusers(user);
			
			
			
			
				model.addAttribute("order", payment);
			
		}
		catch(Exception e) {
			OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
			String emailUser = token.getPrincipal().getAttributes().get("email").toString();
			if (userRepository.findUserByEmail(emailUser).isPresent()) {
				Optional<User> userData = userRepository.findUserByEmail(emailUser);

				
				userData.ifPresent(user1 -> {
					
					
					
					List<Payment> payment3 = paymentService.findByusers(user1);
					  for(Object r : payment3) {
						  
						  Payment   payment = new Payment();
					  payment =  (Payment) r;
					  String status = payment.getStatus();
		
					}
					 
					  model.addAttribute("order", payment3);
	
				});
				
				
				
			}
			
			
		}
		GlobalData.cart.clear();
			
		return "orderData";
		}
	
	@GetMapping("/orderData/viewData/{id}")
	public String getOrderItems(@PathVariable int id,Model model) {
		
		Optional<Payment> payment = paymentService.findByusers(id);
		Payment payment3 =payment.get();
		
		List<OrderItem> daa = orderRepository.findAll();
	
		
		model.addAttribute("hatib", daa);
		
		model.addAttribute("payment", payment3);
	
		
		
		return"orderView";
	}
	
	
	@PostMapping("/orderData/submitData")
	public String putDataOnDb(@ModelAttribute("paymeny")Payment pt,HttpServletRequest request,Authentication authentication,Model model) {
	
	
		String s= LocalTime.now()
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_LOCAL_TIME);
		
		pt.setOrderRecivedTime(s);
		pt.setOrderStatus("Delivered");
		
		paymentService.addPayment(pt);
		
Principal p = request.getUserPrincipal();
		
		if(p != null) {
			
			try {
			
			User user = getUserDetails();
			
		
			
			
			
			
			List<Role> rl = user.getRoles();
			
		
			User user1 = userRepository.findByEmail(user.getEmail());
			request.setAttribute("username", user.getFirstName());
			for(Role r1:rl) {
				model.addAttribute("role", r1.getName());
				break;
			}
			
			
			}
			catch(Exception e) {
				OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
				String emailUser = token.getPrincipal().getAttributes().get("email").toString();
				
				User usermain = this.userRepository.findByEmail(emailUser);
			
				
				request.setAttribute("username", usermain.getFirstName());
				model.addAttribute("role", null);
			}
			
			
			
			
			
		}
		
		
		
		return "index";
	}
	
	public User getUserDetails() {
		User user = new User();
		user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}
	

}
