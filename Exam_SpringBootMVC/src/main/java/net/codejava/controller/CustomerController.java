package net.codejava.controller;

import net.codejava.model.Customers;
import net.codejava.service.CustomerService;
import net.codejava.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;  // Import thêm lớp LocalDateTime

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmailService emailService;

    
    @GetMapping("/index")
    public String dashboard() {
        return "customer/cus_index";
    }

    
    @GetMapping("/login")
    public String loginForm() {
        return "customer/cus_login";
    }

    
    @PostMapping("/chklogin")
    public String checkLogin(@RequestParam("email") String email,
                             @RequestParam("pwd") String password,
                             HttpServletRequest request,
                             HttpServletResponse response) {
    	logger.info("Checking login for email: " + email);

        Customers customer = customerService.findByEmail(email);

        if (customer != null) {
            logger.info("User found: " + email);
            
            if (customer.getStatus() == 0) {
                logger.warn("Attempt to login with unverified account: " + email);
                return "redirect:/customer/login?unverified=true";
            }

            if (customerService.checkPassword(password, customer.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("customer", customer);
                logger.info("User authenticated: " + email);
                return "redirect:/customer/index"; 

            } else {
                logger.error("Password check failed for user: " + email);
                return "redirect:/customer/login?error=wrongPassword"; 
            }
        } else {
            logger.error("User not found: " + email);
            return "redirect:/customer/login?error=wrongPassword"; 
        }
    }


    
    @GetMapping("/register")
    public String registerForm() {
        return "customer/cus_register";
    }

    
    @PostMapping("/register")
    public String registerUser(@RequestParam("fullname") String fullname,
                               @RequestParam("pwd") String password,
                               @RequestParam("email") String email,
                               @RequestParam("phone") String phone,
                               @RequestParam("address") String address,
                               @RequestParam("profileImage") String profileImage,
                               @RequestParam("status") int status,
                               HttpServletRequest request) {

    	if (customerService.findByEmail(email) != null) {
            return "redirect:/customer/register?error=emailExists"; 
        }
        
        Customers newCustomer = new Customers();
        newCustomer.setFullname(fullname);
        newCustomer.setPassword(password);  
        newCustomer.setEmail(email);
        newCustomer.setPhone(phone);
        newCustomer.setAddress(address);
        newCustomer.setProfileImage(profileImage);
        newCustomer.setStatus(status);
        newCustomer.setCreatedAt(LocalDateTime.now());  

        
        String siteURL = ServletUriComponentsBuilder.fromRequestUri(request)
                        .replacePath(null)
                        .build()
                        .toUriString();
        customerService.saveUser(newCustomer, siteURL);
        return "redirect:/customer/login?registered=true";
    }

    
 // Verify account
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("code") String code, Model model) {
        Customers customer = customerService.findByVerifyCode(code);
        
        if (customer == null) {
            model.addAttribute("message", "Invalid verification code.");
            return "customer/cus_verify"; 
        }
        
        // Activate user
        customer.setStatus(1);
        customer.setVerifyCode(null); 
        customerService.updateUser(customer);
        
        model.addAttribute("message", "Your account has been verified. You can now log in.");
        return "customer/cus_verify"; 
    }
    
 // Lấy thông tin khách hàng
    @GetMapping("/info")
    public String getCustomerInfo(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Customers customer = (Customers) session.getAttribute("customer");

        if (customer != null) {
            Customers customerInfo = customerService.getCustomerInfo(customer.getCustomerId());
            model.addAttribute("customerInfo", customerInfo);
            return "customer/cus_info"; // Chỉ định view để hiển thị thông tin
        }
        return "redirect:/customer/login"; // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
    }

    // Cập nhật thông tin khách hàng
    @PostMapping("/update")
    public String updateCustomerInfo(@ModelAttribute("customerInfo") Customers customerInfo, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Customers currentCustomer = (Customers) session.getAttribute("customer");

        if (currentCustomer != null) {
            customerInfo.setCustomerId(currentCustomer.getCustomerId()); 
            logger.info("Updating customer info: " + customerInfo);
            if (customerService.updateCustomerInfo(customerInfo)) {
                session.setAttribute("customer", customerInfo); 
                return "redirect:/customer/info?updateSuccess=true"; 
            }
        }
        return "redirect:/customer/info?updateError=true"; 
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); 
        }
        return "redirect:/customer/login"; 
    }

}
