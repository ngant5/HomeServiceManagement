package net.codejava.controller;

import net.codejava.model.Employees;
import net.codejava.service.EmailService;
import net.codejava.service.EmployeeService;
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

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private EmailService emailService;
    
    // Display the admin dashboard
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/ad_index";
    }
    
    // Display the login form
    @GetMapping("/login")
    public String loginForm() {
        return "admin/ad_login";
    }
    
    // Handle login
    @PostMapping("/chklogin")
    public String checkLogin(@RequestParam("email") String email,
                             @RequestParam("pwd") String password,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        Employees employee = employeeService.findByEmail(email);
        
        if (employee != null) {
            if (employee.getStatus() == 0) {
                // Account not verified
                logger.warn("Attempt to login with unverified account: " + email);
                return "redirect:/admin/login?unverified=true";
            }
            
            if (employeeService.checkPassword(password, employee.getPassword())) {
                // Save user info in session
                HttpSession session = request.getSession();
                session.setAttribute("employee", employee);
                
                logger.info("User authenticated: " + email + ", UserType: " + employee.getUserType());
                
                // Redirect based on user type
                switch (employee.getUserType().toUpperCase()) {
                    case "ADMIN":
                        return "redirect:/admin/dashboard";
                    case "EMPLOYEE":
                        return "redirect:/employee/dashboard";
                    default:
                        logger.warn("Unknown userType: " + employee.getUserType());
                        return "redirect:/admin/login?error=true";
                }
            } else {
                logger.error("Password check failed for user: " + email);
            }
        } else {
            logger.error("User not found: " + email);
        }
        
        return "redirect:/admin/login?error=true";
    }

    // Display registration form
    @GetMapping("/register")
    public String registerForm() {
        return "admin/ad_register";
    }
    
    // Handle new admin registration
    @PostMapping("/register")
    public String registerUser(@RequestParam("fullname") String fullname,
                               @RequestParam("pwd") String password,
                               @RequestParam("userType") String userType,
                               @RequestParam("email") String email,
                               @RequestParam("phone") String phone,
                               @RequestParam("address") String address,
                               @RequestParam("profileImage") String profileImage,
                               @RequestParam("experienceYears") int experienceYears,
                               @RequestParam("salary") int salary,
                               @RequestParam("status") int status,
                               HttpServletRequest request) {
        
        // Create new employee
        Employees newEmployee = new Employees();
        newEmployee.setFullname(fullname);
        newEmployee.setPassword(password); // Will be encrypted in service
        newEmployee.setUserType(userType);
        newEmployee.setEmail(email);
        newEmployee.setPhone(phone);
        newEmployee.setAddress(address);
        newEmployee.setProfileImage(profileImage);
        newEmployee.setExperienceYears(experienceYears);
        newEmployee.setSalary(salary);
        newEmployee.setStatus(status);

        // Get site URL
        String siteURL = ServletUriComponentsBuilder.fromRequestUri(request)
                        .replacePath(null)
                        .build()
                        .toUriString();

        // Save user and send verification email
        employeeService.saveUser(newEmployee, siteURL);
        
        logger.info("New user registered: " + fullname + ", UserType: " + userType);
        
        // Redirect to login with a message to verify email
        return "redirect:/admin/login?registered=true";
    }
    
    // Verify account
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("code") String code, Model model) {
        Employees employee = employeeService.findByVerifyCode(code);
        
        if (employee == null) {
            model.addAttribute("message", "Invalid verification code.");
            return "admin/ad_verify"; // Create a view to show messages
        }
        
        // Activate user
        employee.setStatus(1);
        employee.setVerifyCode(null); // Clear verification code
        employeeService.updateUser(employee);
        
        model.addAttribute("message", "Your account has been verified. You can now log in.");
        return "admin/ad_verify"; // Create a view to show success message
    }
}