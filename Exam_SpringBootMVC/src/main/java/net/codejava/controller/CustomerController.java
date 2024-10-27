package net.codejava.controller;

import net.codejava.model.Customers;
import net.codejava.model.Employees;
import net.codejava.model.Services;
import net.codejava.service.CustomerService;
import net.codejava.service.EmailService;
import net.codejava.service.EmployeeService;
import net.codejava.service.ServiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List; 

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private ServiceService servicesService;
    
    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmailService emailService;
    
    private static final String UPLOAD_DIR = "uploads/";

    
    @GetMapping("/index")
    public String showIndex(Model model) {
        List<Services> servicesList = servicesService.getAllServices();
        List<Employees> employeesList = employeeService.getAllEmployees(); 
        model.addAttribute("services", servicesList);
        model.addAttribute("employees", employeesList);
        return "customer/index"; 
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
        	logger.info("User found: {}", email);

            
            if (customer.getStatus() == 0) {
                logger.warn("Attempt to login with unverified account: " + email);
                return "redirect:/customer/login?unverified=true";
            }

            if (customerService.checkPassword(password, customer.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("customer", customer);
                logger.info("User authenticated: {}", email);
                return "redirect:/customer/cus_mypage"; 

            } else {
                logger.error("Password check failed for user: " + email);
                return "redirect:/customer/login?error=wrongPassword"; 
            }
        } else {
            logger.error("User not found: " + email);
            return "redirect:/customer/login?error=wrongPassword"; 
        }
    }

    @GetMapping("/cus_mypage")
    public String viewMyPage(Model model, HttpSession session) {
        Customers customer = (Customers) session.getAttribute("customer");
        if (customer == null) {
            logger.info("Customer not found in session, redirecting to login");
            return "redirect:/customer/login";
        }
        
        logger.info("Customer found: " + customer.getFullname());
        model.addAttribute("customer", customer);
        return "customer/cus_mypage";
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
    public String updateProfile(@RequestParam("fullname") String fullname,
                                @RequestParam("phone") String phone,
                                @RequestParam("address") String address,
                                @RequestParam("profileImage") MultipartFile profileImage,
                                HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Customers customer = (Customers) session.getAttribute("customer");

        if (customer == null) {
            return "redirect:/customer/login";
        }

        // Ensure the upload directory exists
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // Create the directory if it doesn't exist
        }

        // Update employee details
        customer.setFullname(fullname);
        customer.setPhone(phone);
        customer.setAddress(address);

        // Handle profile image upload
        if (!profileImage.isEmpty()) {
            try {
                // Check for existing profile image
                String existingImageName = customer.getProfileImage(); // Get the old image name
                if (existingImageName != null && !existingImageName.isEmpty()) {
                    // Create a File object for the existing image
                    File existingImageFile = new File(UPLOAD_DIR + existingImageName);
                    
                    // Delete the existing image file if it exists
                    if (existingImageFile.exists()) {
                        boolean deleted = existingImageFile.delete();
                        if (!deleted) {
                            model.addAttribute("error", "Failed to delete old image.");
                            return "customer/cus_info"; // Return to the update form on error
                        }
                    }
                }

                // Construct the path to save the uploaded file
                String newFileName = profileImage.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR + newFileName);

                // Write the new file to the specified path
                Files.write(uploadPath, profileImage.getBytes());

                // Save only the new filename in the database
                customer.setProfileImage(newFileName); // Save only the new filename, not the path
            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload image.");
                return "customer/cus_info"; // Return to the update form on error
            }
        }

        // Update the employee information in the database
        customerService.updateCustomerInfo(customer);

        // Update session
        session.setAttribute("customer", customer);
        return "redirect:/customer/index"; // Redirect to the dashboard after successful update
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); 
        }
        return "redirect:/customer/login"; 
    }
    
    @GetMapping("/change-password")
    public String changePasswordForm(Model model, HttpSession session) {
        Customers customer = (Customers) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/customer/login"; 
        }
        model.addAttribute("customer", customer);
        return "customer/cus_change_password"; 
    }

    
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session, Model model) {
    	Customers customer = (Customers) session.getAttribute("customer");
        
        if (customer == null) {
            return "redirect:/customer/login";
        }

        
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match.");
            return "customer/cus_change_password";
        }

        
        boolean success = customerService.changePassword(customer, currentPassword, newPassword);

        if (success) {
            model.addAttribute("message", "Password changed successfully.");
            return "customer/index"; 
        } else {
            model.addAttribute("error", "Current password is incorrect.");
            return "customer/cus_change_password";
        }
    }
    
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "customer/cus_forgot_password"; 
    }

    // Handle forgot password submission
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, HttpServletRequest request) {
        String siteURL = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        customerService.generateResetToken(email, siteURL);
        
        return "redirect:/customer/forgot-password?sent=true"; // Redirect to confirm the email was sent
    }
    
 // Show reset password form
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Customers customer = customerService.findByResetToken(token);

        if (customer == null) {
            model.addAttribute("message", "Invalid or expired reset token.");
            return "customer/cus_reset_password"; // Create this view
        }

        model.addAttribute("token", token); // Pass the token to the form
        return "customer/cus_reset_password";
    }

    // Handle reset password submission
    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token,
                                      @RequestParam("newPassword") String newPassword,
                                      @RequestParam("confirmPassword") String confirmPassword,
                                      Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("token", token);
            return "customer/cus_reset_password";
        }

        boolean success = customerService.resetPassword(token, newPassword);

        if (success) {
            model.addAttribute("message", "Your password has been reset. You can now log in.");
        } else {
            model.addAttribute("message", "Invalid or expired reset token.");
        }

        return "customer/cus_reset_password";
    }
}
