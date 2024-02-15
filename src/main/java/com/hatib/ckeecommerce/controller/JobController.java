package com.hatib.ckeecommerce.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.hatib.ckeecommerce.model.OrderItem;
import com.hatib.ckeecommerce.model.Payment;
import com.hatib.ckeecommerce.model.User;
import com.hatib.ckeecommerce.service.OrderService;
import com.hatib.ckeecommerce.service.PaymentService;

@Controller
public class JobController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	PaymentService paymentService;
	
	
	@GetMapping("/working")
	public String adminPanel() {
		return "workerHome";
	}
	
	
	@GetMapping("/working/orderDetails")
	public String getOrder(Model model) {
		
		List<OrderItem> od  =orderService.findByAll();
	
		
		model.addAttribute("OrderItem", od);
		
		
		return"orderHome";
	}
	
	@GetMapping("/working/viewData/{id}")
	public String viewData(@PathVariable int id,Model model) {
		
		
		
		
		Optional<Payment> pt = paymentService.findById(id);
		
		Payment pr = pt.get();
		model.addAttribute("paymentData", pr);
		
		return "OrderAccept";
	}
	
	
	@PostMapping("/working/timeLeft")
	public String timeGenerate(@ModelAttribute("paymentData")Payment payment) throws Exception {
		
		System.out.println(payment.getId());
		System.out.println(payment.getUsers().getId());
		
		Payment pt = payment;
		pt.setOrderStatus("Accept");
		
		CartController c = new CartController();
	
		
		String s= LocalTime.now()
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_LOCAL_TIME);
		pt.setOrderAcceptTime(s);
		
		User user = new User();
		user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		pt.setWorker(user);
		
		paymentService.addPayment(pt);
		
		return"TimeLeft";
	}
	
	@GetMapping("/working/checkYourCompletedOrder")
	public String getCompleteOrder(Model model,HttpServletRequest request,HttpSession session) {
		User user = new User();	
		user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<Payment> pt = paymentService.findAllByworker(user);
		
		model.addAttribute("payment", pt);
		
		
		return "WorkerCompletedOrder";
	}

	
	@GetMapping("/working/download")
	public String excelSheet(HttpServletResponse response,HttpServletRequest request,	HttpSession session) throws IOException {
 

		XSSFWorkbook workbook = new XSSFWorkbook();
		
		XSSFSheet sheet = workbook.createSheet("Completed-Order");
		
		
		XSSFRow rowhead = sheet.createRow((short)0); 
		
		XSSFFont font = workbook.createFont();
		XSSFCellStyle style = workbook.createCellStyle();
		font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		font.setFontHeightInPoints((short)10);
		font.setBold(true);
		style.setFont(font);
		
		rowhead.createCell(0).setCellValue("S.No.");  
		
		rowhead.createCell(1).setCellValue("Customer Name");  
		rowhead.createCell(2).setCellValue("Date"); 
		rowhead.createCell(3).setCellValue("AcceptTime");  
		rowhead.createCell(4).setCellValue("DeliverTime");  
		rowhead.createCell(5).setCellValue("Amount");
		rowhead.createCell(6).setCellValue("OrderId");
		rowhead.createCell(7).setCellValue("Rating");
		
		for(int j = 0; j<=6; j++) {
			rowhead.getCell(j).setCellStyle(style);
	}
	
		
		
		User user = new User();	
		user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<Payment> pt = paymentService.findAllByworker(user);
		String pattern = "dd-M-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		int rownum =1;
		for(Payment pay: pt) {
			if(pay.getOrderRecivedTime() != null)
			{
			XSSFRow row = sheet.createRow((short)rownum); 
			
			row.createCell(0).setCellValue(rownum);  
			row.createCell(1).setCellValue(pay.getFirstName());  
			String dateForData = simpleDateFormat.format(new Date(pay.getDate()));
			row.createCell(2).setCellValue(dateForData); 
			row.createCell(3).setCellValue(pay.getOrderAcceptTime());  
			row.createCell(4).setCellValue(pay.getOrderRecivedTime());  
			row.createCell(5).setCellValue(pay.getAmount());  
			row.createCell(6).setCellValue(pay.getOrderId());  
			row.createCell(7).setCellValue(pay.getStar());  
			
			
			
			for(int i=0;i<= 7;i++) {
				sheet.autoSizeColumn(i);
			}
			
			rownum++;
			}
		}
		
	
		
		String home = System.getProperty("user.home");
		
		
		System.out.println(home);
		FileOutputStream fl = new FileOutputStream(home+"/Downloads/"+user.getFirstName()+"-"+date+"Completed.xlsx");

		workbook.write(fl);
		
		workbook.close();
		
	
		
       
		return "redirect:/working/checkYourCompletedOrder";
		
	}
	
	
}
