package com.hatib.ckeecommerce.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hatib.ckeecommerce.Global.GlobalData;
import com.hatib.ckeecommerce.dto.ProductDTO;
import com.hatib.ckeecommerce.model.JobDetails;
import com.hatib.ckeecommerce.model.Payment;
import com.hatib.ckeecommerce.model.Product;
import com.hatib.ckeecommerce.model.Role;
import com.hatib.ckeecommerce.model.User;
import com.hatib.ckeecommerce.repository.UserRepository;
import com.hatib.ckeecommerce.service.CategoryService;
import com.hatib.ckeecommerce.service.JobDetailsService;
import com.hatib.ckeecommerce.service.ProductService;

@Controller
public class HomeController {

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JobDetailsService jobDetailsService;
	
	public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/jobDetailsImages";
	
	@GetMapping({"/","/home"})
	public String home(Model model,HttpServletRequest request,Authentication authentication) {
		
		model.addAttribute("cartCount",GlobalData.cart.size());
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
	
	@GetMapping("/shop")
	public String shop(Model model) {
		model.addAttribute("cartCount",GlobalData.cart.size());
		model.addAttribute("categories",categoryService.getAllCategory());
		model.addAttribute("products",productService.getAllProducts());
		return "shop";
	}
	
	@GetMapping("/shop/category/{id}")
	public String shopByCategory(Model model,@PathVariable Long id) {
		model.addAttribute("cartCount",GlobalData.cart.size());
		model.addAttribute("categories",categoryService.getAllCategory());
		model.addAttribute("products",productService.getProductByCategoryId(id));
		return "shop";
	}
	
	@GetMapping("/shop/viewproduct/{id}")
	public String viewProduct(Model model,@PathVariable Long id) {
		model.addAttribute("cartCount",GlobalData.cart.size());
//		model.addAttribute("categories",categoryService.getAllCategory());
		model.addAttribute("product",productService.getProductById(id).get());
		return "viewProduct";
	}
	
	@GetMapping("/findMyJob/jobDetails")
	public String getJobDetails(HttpServletRequest req ,HttpServletResponse res,Model model,Authentication authentication) {
		
		try {
			User user = new User();
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			List<JobDetails> jd = jobDetailsService.findByusers(user);
		
		
			
			List<Role> roles = user.getRoles();
			
			
			
				model.addAttribute("jobDetails", jd);
				model.addAttribute("roles",roles);
				return"jobDetails";
			
			
			
			
		}
		catch(Exception e) {
//			OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
//			String emailUser = token.getPrincipal().getAttributes().get("email").toString();
//			if (userRepository.findUserByEmail(emailUser).isPresent()) {
//				Optional<User> userData = userRepository.findUserByEmail(emailUser);
//
//				
//				userData.ifPresent(user1 -> {
//					
//					
//					
//					List<JobDetails> jd = jobDetailsService.findByusers(user1);
//					
//					List<Role> roles = user1.getRoles();
//					
//					 model.addAttribute("jobDetails", jd);
//					 req.setAttribute("roles", roles);
//					 
//					 
//					
//	
//				});
//				
//				
//				
//			}
			return"index";
			
		}
		
		
	}

	@GetMapping("/findMyJob/formSubmit")
	public String getFormForJob(Model model) {
		
		model.addAttribute("jobDetails", new JobDetails());
		
		return"jobForm";
		
	}
	
	
	
	
	
	@PostMapping("/findMyJob/formSubmit")
	public String saveProducts(@ModelAttribute("jobDetails")JobDetails jobDetails,@RequestParam("numPlate")MultipartFile file,@RequestParam("imgName")String imgName,Authentication authentication) throws IOException{
	
		JobDetails jd = new JobDetails();
		jd.setFirstName(jobDetails.getFirstName());
		jd.setLastName(jobDetails.getLastName());
		jd.setTypeOfJob(jobDetails.getTypeOfJob());
		jd.setPhone(jobDetails.getPhone());
		jd.setZipCode(jobDetails.getZipCode());
		jd.setAddress(jobDetails.getAddress());
		jd.setQuality(jobDetails.getQuality());
		jd.setVehicleNo(jobDetails.getVehicleNo());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		jd.setDate(dtf.format(now));
		jd.setStatus("Pending");
		
		User user = new User();
		
		try {
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			jd.setUsers(user);
		} catch (Exception e) {
			OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
			String emailUser = token.getPrincipal().getAttributes().get("email").toString();
			if (userRepository.findUserByEmail(emailUser).isPresent()) {
				Optional<User> userData = userRepository.findUserByEmail(emailUser);

				userData.ifPresent(user1 -> {
					jd.setUsers(user1);
				});

//				List<User> hat =userData.stream().collect(Collectors.toList());

			}
		}
        String imageUUID;
		
		if(!file.isEmpty()) {
			imageUUID = file.getOriginalFilename();
			Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
			Files.write(fileNameAndPath, file.getBytes());
		}else {
          imageUUID =imgName;
		}
		jd.setImageName(imageUUID);
		
		this.jobDetailsService.addJobDetails(jd);
		
		
		
		
		
		return "index";
	}
	
	
	public User getUserDetails() {
		User user = new User();
		user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}
	
	
	
	
	
}
