package net.codejava.controller;

import net.codejava.model.Contracts;
import net.codejava.model.Customers;
import net.codejava.model.Employees;
import net.codejava.model.Payments;
import net.codejava.service.ContractService;
import net.codejava.service.CustomerService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ContractService contractService;
    
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
    	List<Customers> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);

        List<Contracts> contracts = customerService.getAllContracts();
        model.addAttribute("contracts", contracts);

        
        return "admin/dashboard";
    }
    
    
    @GetMapping("/login")
    public String loginForm() {
        return "admin/ad_login";
    }
    
    
    @PostMapping("/chklogin")
    public String checkLogin(@RequestParam("email") String email,
                             @RequestParam("pwd") String password,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        Employees employee = employeeService.findByEmail(email);
        
        if (employee != null) {
            if (employee.getStatus() == 0) {
                
                logger.warn("Attempt to login with unverified account: " + email);
                return "redirect:/admin/login?unverified=true";
            }
            
            if (employeeService.checkPassword(password, employee.getPassword())) {
                
                HttpSession session = request.getSession();
                session.setAttribute("employee", employee);
                
                logger.info("User authenticated: " + email + ", UserType: " + employee.getUserType());
                
                
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

    
    @GetMapping("/change-password")
    public String changePasswordForm(Model model, HttpSession session) {
        Employees employee = (Employees) session.getAttribute("employee");
        if (employee == null) {
            return "redirect:/admin/login"; // Redirect to login if not authenticated
        }
        model.addAttribute("employee", employee);
        return "admin/pages/ad_change_password"; // Create this view
    }

    // Handle change password form submission
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session, Model model) {
        Employees employee = (Employees) session.getAttribute("employee");
        
        if (employee == null) {
            return "redirect:/admin/login";
        }

        // Check if the new passwords match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match.");
            return "admin/ad_change_password";
        }

        // Try to change the password
        boolean success = employeeService.changePassword(employee, currentPassword, newPassword);

        if (success) {
            model.addAttribute("message", "Password changed successfully.");
            return "admin/ad_change_password"; 
        } else {
            model.addAttribute("error", "Current password is incorrect.");
            return "admin/ad_change_password";
        }
    }
    
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "admin/ad_forgot_password"; 
    }

    // Handle forgot password submission
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, HttpServletRequest request) {
        String siteURL = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        Employees admin = employeeService.findByEmail(email);
        if (admin != null) {
            String token = UUID.randomUUID().toString(); 
            admin.setResetToken(token); 
            employeeService.updateUser(admin); 

            
            String resetURL = siteURL + "/admin/reset-password?token=" + token;

            
            emailService.sendResetPasswordEmailforAdmin(admin, resetURL);
        }
        
        return "redirect:/admin/forgot-password?sent=true"; // Redirect để xác nhận email đã gửi

    }

    // Show reset password form
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Employees employee = employeeService.findByResetToken(token);

        if (employee == null) {
            model.addAttribute("message", "Invalid or expired reset token.");
            return "admin/ad_reset_password"; // Create this view
        }

        model.addAttribute("token", token); // Pass the token to the form
        return "admin/ad_reset_password";
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
            return "admin/ad_reset_password";
        }

        boolean success = employeeService.resetPassword(token, newPassword);

        if (success) {
            model.addAttribute("message", "Your password has been reset. You can now log in.");
        } else {
            model.addAttribute("message", "Invalid or expired reset token.");
        }

        return "admin/ad_reset_password";
    }
    
    @GetMapping("/admin/contracts")
    public String listContracts(Model model) {
        List<Contracts> contracts = contractService.getAllContracts();
        model.addAttribute("contracts", contracts);
        return "admin/ad_contracts/"; 
    }

    
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("code") String code, Model model) {
        Employees employee = employeeService.findByVerifyCode(code);
        
        if (employee == null) {
            model.addAttribute("message", "Invalid verification code.");
            return "admin/ad_verify"; 
        }
        
        
        employee.setStatus(1);
        employee.setVerifyCode(null); 
        employeeService.updateUser(employee);
        
        model.addAttribute("message", "Your account has been verified. You can now log in.");
        return "admin/ad_verify"; 
    }
    
    @GetMapping("/customers")
    public String getAllCustomers(Model model) {
        List<Customers> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "admin/ad_customers"; 
    }

    @GetMapping("/customers/{customerId}")
    public String getCustomerContracts(@PathVariable int customerId, Model model) {
        List<Contracts> contracts = customerService.getCustomerContracts(customerId);
        if (contracts == null) {
            model.addAttribute("errorMessage", "No contracts found for this customer.");
            return "error"; 
        }
        
        model.addAttribute("contracts", contracts);
        return "admin/ad_contracts_by_cus"; 
    }

    @GetMapping("/contracts/{contractId}")
    public String getContractById(@PathVariable int contractId, Model model) {
        Contracts contract = contractService.getContractById(contractId);
        if (contract == null) {
            model.addAttribute("errorMessage", "Contract Not Found");
            return "error"; 
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDate = contract.getCreatedAt().format(formatter);
        
        model.addAttribute("contract", contract);
        model.addAttribute("formattedCreatedAt", formattedDate);
        
        return "admin/ad_contract_detail"; 

    }

    @GetMapping("/contracts/{contractId}/employee")
    public Employees getEmployeeByContract(@PathVariable int contractId) {
        return customerService.getEmployeeByContract(contractId);
    }

   


    @GetMapping("/contracts")
    public String getAllContracts(Model model) {
        List<Contracts> contracts = contractService.getAllContracts();
        model.addAttribute("contracts", contracts);
        return "admin/ad_contracts"; 
    }

    @GetMapping("/contracts/duration")
    public List<Contracts> findContractsByDuration(@RequestParam int duration) {
        return customerService.findContractsByDuration(duration);
    }

    @GetMapping("/contracts/{contractId}/payments")
    public List<Payments> getPaymentsByContract(@PathVariable int contractId) {
        return customerService.getPaymentsByContract(contractId);
    }

    @PostMapping("/contracts/{contractId}/reminder")
    public void sendPaymentReminder(@PathVariable int contractId) {
        customerService.sendPaymentReminder(contractId);
    }

    @PutMapping("/contracts/{contractId}/due-date")
    public void setPaymentDueDate(@PathVariable int contractId, @RequestParam LocalDate dueDate) {
        LocalDateTime dueDateTime = dueDate.atStartOfDay(); 
        customerService.setPaymentDueDate(contractId, dueDateTime);
    }


    @PutMapping("/contracts/{contractId}/employee")
    public void updateContractEmployee(@PathVariable int contractId, @RequestBody Employees employee) {
    	int employeeId = employee.getEmployeeId(); 
        customerService.updateContractEmployee(contractId, employeeId);
    }
}
