package com.hatib.ckeecommerce.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hatib.ckeecommerce.Global.GlobalData;
import com.hatib.ckeecommerce.model.Role;
import com.hatib.ckeecommerce.model.User;
import com.hatib.ckeecommerce.repository.RoleRepository;
import com.hatib.ckeecommerce.repository.UserRepository;
import com.hatib.ckeecommerce.service.EmailService;

@Controller
public class LoginController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private EmailService emailService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@GetMapping("/login")
	public String login() {
		GlobalData.cart.clear();
		return "login";
	}

	@GetMapping("/register")
	public String registerget() {
		return "register";
	}

	@PostMapping("/register")
	public String registerPost(@ModelAttribute("user") User user, HttpServletRequest request, HttpSession session)
			throws ServletException, ParseException {

//		String password = user.getPassword();
//		user.setPassword(bCryptPasswordEncoder.encode(password));
//		List<Role> roles= new ArrayList<>();
//		roles.add(roleRepository.findById(2).get());
//		user.setRoles(roles);
//		userRepository.save(user);
//		request.login(user.getEmail(), password);

		String numbers = "0123456789";
		Random rndm_method = new Random();
		char[] jtp = new char[6];
		for (int i = 0; i < 6; i++) {
			jtp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		int otp = Integer.parseInt(new String(jtp));

		String message = "" + "<div style='border:1px solid #e2e2e2;padding:20px;'>" + "<h1>" + "OTP is : " + "<b>"
				+ otp + "</b>" + "</h1>" + "</div>";

		boolean flag = this.emailService.sendEmail("Otp For Register Email", message, user.getEmail());

		session.setAttribute("user", user);
		session.setAttribute("otp", otp);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

		String time = dtf.format(LocalDateTime.now()).toString();

		Date date1 = simpleDateFormat.parse(time);

		session.setAttribute("date", date1);

		session.removeAttribute("message");
		return "registerOtp";
	}

	@PostMapping("/register/verify-otp")
	public String registerOtp(@RequestParam("otp") int otp, HttpSession session, HttpServletRequest request)
			throws ServletException, ParseException {

		User user = (User) session.getAttribute("user");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

		String time = dtf.format(LocalDateTime.now()).toString();
		Date date1 = simpleDateFormat.parse(time);

		Date date2 = (Date) session.getAttribute("date");

		long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());

		long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;

		int myOtp=0;
		if (differenceInMinutes > 0) {
			otp =1;
		}else {
			myOtp = (int) session.getAttribute("otp");
		}

		if (myOtp == otp) {

			User usernew = this.userRepository.findByEmail(user.getEmail());
			if (usernew == null) {

				String password = user.getPassword();
				user.setPassword(bCryptPasswordEncoder.encode(password));
				List<Role> roles = new ArrayList<>();
				roles.add(roleRepository.findById(2).get());
				user.setRoles(roles);
				userRepository.save(user);

				session.setAttribute("messagenew", "Registered Successfully !!");

				session.removeAttribute("user");
				return "login";

			} else {
				session.setAttribute("message", "Email Id Already Registered !!");

				return "registerOtp";
			}

		} else {
			session.setAttribute("message", "Otp Is Wrong");
			return "registerOtp";
		}

	}

	@GetMapping("/forgotPassword")
	public String forgotPassword(HttpSession session) {
		session.removeAttribute("message");
		return "forgetpassword";
	}

	@PostMapping("/forgotPassword/sendOtp")
	public String sendOtp(@RequestParam("email") String email, HttpSession httpSession) throws Exception {

		// generating otp of 4 digit

		String numbers = "0123456789";
		Random rndm_method = new Random();
		
		char[] jtp = new char[6];
		for (int i = 0; i < 6; i++) {
			jtp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		int otp = Integer.parseInt(new String(jtp));

		String message = "" + "<div style='border:1px solid #e2e2e2;padding:20px;'>" + "<h1>" + "OTP is : " + "<b>"
				+ otp + "</b>" + "</h1>" + "</div>";

		boolean flag = this.emailService.sendEmail("Otp For Forgot Password", message, email);

		User user = this.userRepository.findByEmail(email);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

		String time = dtf.format(LocalDateTime.now()).toString();

		Date date1 = simpleDateFormat.parse(time);

		if (flag && user != null) {
			httpSession.setAttribute("otp", otp);
			httpSession.setAttribute("email", email);
			httpSession.setAttribute("date", date1);

			return "verifyOtp";
		} else {

			httpSession.setAttribute("message", "Email Is Not Registered Or Not Exist !!");
			return "forgetpassword";
		}

	}

	@PostMapping("/forgotPassword/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session) throws ParseException {

		
		String email = (String) session.getAttribute("email");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

		String time = dtf.format(LocalDateTime.now()).toString();
		Date date1 = simpleDateFormat.parse(time);

		Date date2 = (Date) session.getAttribute("date");

		long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());

		// Calculating the difference in Minutes
		long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;
		
		int myOtp=0;

		if (differenceInMinutes > 0) {
		}else {
			myOtp= (int) session.getAttribute("otp");
		}

		if (myOtp == otp) {

			User user = this.userRepository.findByEmail(email);
			if (user == null) {
				session.setAttribute("message", "Email Id Not Registered !!");
				return "forgetpassword";
			} else {
				return "change-password";
			}

		} else {
			session.setAttribute("message", "Otp Is Wrong");
			return "verifyOtp";
		}
	}

	@PostMapping("/forgotPassword/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody Map<String, Object> data, HttpSession session) {

		String email = (String) session.getAttribute("email");

		String password = data.get("password").toString();

		User user = this.userRepository.findByEmail(email);
		user.setPassword(new BCryptPasswordEncoder().encode(password));

		this.userRepository.save(user);

		return ResponseEntity.ok(Map.of("msg", "updated"));

	}

}