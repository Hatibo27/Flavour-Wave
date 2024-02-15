package com.hatib.ckeecommerce.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hatib.ckeecommerce.dto.ProductDTO;
import com.hatib.ckeecommerce.model.Category;
import com.hatib.ckeecommerce.model.JobDetails;
import com.hatib.ckeecommerce.model.Product;
import com.hatib.ckeecommerce.model.Role;
import com.hatib.ckeecommerce.model.User;
import com.hatib.ckeecommerce.repository.RoleRepository;
import com.hatib.ckeecommerce.repository.UserRepository;
import com.hatib.ckeecommerce.service.CategoryService;
import com.hatib.ckeecommerce.service.JobDetailsService;
import com.hatib.ckeecommerce.service.ProductService;


@Controller
public class AdminPanelController {
	
	public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";
	public static String uploadDir1 = System.getProperty("user.dir") + "/src/main/resources/static/jobDetailsImages";
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	JobDetailsService jobDetailsService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;

	@GetMapping("/admin")
	public String adminPanel() {
		return "adminHome";
	}
	
	@GetMapping("/admin/categories")
	public String getCat(Model model) {
		model.addAttribute("categories",categoryService.getAllCategory());
		return "categories";
	}
	
	@GetMapping("/admin/categories/add")
	public String getCatAdd(Model model) {
		model.addAttribute("category", new Category());
		return "categoriesAdd";
	}
	
	@PostMapping("/admin/categories/add")
	public String postcatAdd(@ModelAttribute("category")Category category) {
		categoryService.addCategory(category);
		return "redirect:/admin/categories";
	}
	
	@GetMapping("/admin/categories/delete/{id}")
	public String deleteCat(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return "redirect:/admin/categories";
	}
	@GetMapping("/admin/categories/update/{id}")
	public String updateCat(@PathVariable Long id,Model model) {
		Optional<Category> category = categoryService.getCategoryById(id);
		if(category.isPresent()) {
			model.addAttribute("category",category.get());
			return "categoriesAdd";
		}
		else {
			return "404";
		}
		
	}
	
	//product Page
	@GetMapping("/admin/products")
	public String getProducts(Model model) {
		model.addAttribute("products",productService.getAllProducts());
		return "products";
	}
	
	@GetMapping("/admin/products/add")
	public String addProducts(Model model) {
		model.addAttribute("productDTO",new ProductDTO());
		model.addAttribute("categories",categoryService.getAllCategory());
		return "productsAdd";
	}
	
	@PostMapping("/admin/products/add")
	public String saveProducts(@ModelAttribute("productDTO")ProductDTO productDTO,@RequestParam("productImage")MultipartFile file,@RequestParam("imgName")String imgName) throws IOException{
		Product product = new Product();
		
		product.setId(productDTO.getId());
		product.setName(productDTO.getName());
		product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		product.setWeight(productDTO.getWeight());
		
		String imageUUID;
		
		if(!file.isEmpty()) {
			imageUUID = file.getOriginalFilename();
			Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
			Files.write(fileNameAndPath, file.getBytes());
		}else {
          imageUUID =imgName;
		}
		product.setImageName(imageUUID);
		
		productService.addProducts(product);
		
		
		
		return "redirect:/admin/products";
	}
	
	@GetMapping("/admin/product/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		productService.deleteProducts(id);
		return "redirect:/admin/products";
	}
	
	@GetMapping("/admin/product/update/{id}")
	public String updateProducts(@PathVariable Long id,Model model) {
		
	 Product products = productService.getProductById(id).get();
	
	 ProductDTO productDTO = new ProductDTO();
	 productDTO.setId(products.getId());
	 productDTO.setName(products.getName());
	 productDTO.setCategoryId(products.getCategory().getId());
	 productDTO.setDescription(products.getDescription());
	 productDTO.setImageName(products.getImageName());
	 productDTO.setPrice(products.getPrice());
	 productDTO.setWeight(products.getWeight());
	 
	 model.addAttribute("categories",categoryService.getAllCategory());
	 model.addAttribute("productDTO",productDTO);
	 
		return "productsAdd";
		
	}
	
	@GetMapping("/admin/jobAccept")
	public String getJobUpdates(Model model) {
		
		
		
		List<JobDetails>  jd = jobDetailsService.findAll();
		
		model.addAttribute("jobDetails", jd);
		
		return "jobRequests";
		
		
	}
	
	@GetMapping("/admin/viewFullRequests/{id}")
	public String acceptJobUpdates(@PathVariable int id,Model model) throws IOException {
		
		JobDetails jd = jobDetailsService.findById(id).get();
		 ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		 InputStream io = AdminPanelController.class.getResourceAsStream("/static/jobDetailsImages/"+jd.getImageName());
		model.addAttribute("jobDetails", jd);
		
		return "jobAccept";
		
		
	}
	
	@PostMapping("/admin/acceptOrRejectReq")
	public String accOrRej(@ModelAttribute("jobDetails")JobDetails jobDetails ,HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		
		List<Role> roles= new ArrayList<>();
		roles.add(roleRepository.findById(4).get());
		
		List<Role> roles2= new ArrayList<>();
		roles2.add(roleRepository.findById(2).get());
		
		
		User user = new User();
		user.setId(jobDetails.getUsers().getId());
		user.setEmail(jobDetails.getUsers().getEmail());
		user.setFirstName(jobDetails.getUsers().getFirstName());
		user.setLastName(jobDetails.getUsers().getLastName());
		user.setPassword(jobDetails.getUsers().getPassword());
		String status = jobDetails.getStatus();
		
		
		String st = "Approved";
		if(st == status) {	
			user.setRoles(roles);
			
		}else {
			user.setRoles(roles2);
		}
		
		Optional<Role>  jb = roleRepository.findById(4);
		userRepository.save(user);
		JobDetails  jd= new JobDetails();
		jd =jobDetails;
		jobDetailsService.addJobDetails(jd);
		

	    SecurityContextLogoutHandler ctxLogOut = new SecurityContextLogoutHandler();
		if(status=="Approved") {
			 ctxLogOut.logout(request, response, (Authentication) user); 
		}
		
		return "redirect:/admin/jobAccept";
	}
	
	

}
