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

        
        return "admin/pages/dashboard";
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

    
    @GetMapping("/register")
    public String registerForm() {
        return "admin/ad_register";
    }
    
    
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
        
        
        Employees newEmployee = new Employees();
        newEmployee.setFullname(fullname);
        newEmployee.setPassword(password); 
        newEmployee.setUserType(userType);
        newEmployee.setEmail(email);
        newEmployee.setPhone(phone);
        newEmployee.setAddress(address);
        newEmployee.setProfileImage(profileImage);
        newEmployee.setExperienceYears(experienceYears);
        newEmployee.setSalary(salary);
        newEmployee.setStatus(status);

        
        String siteURL = ServletUriComponentsBuilder.fromRequestUri(request)
                        .replacePath(null)
                        .build()
                        .toUriString();

        
        employeeService.saveUser(newEmployee, siteURL);
        
        logger.info("New user registered: " + fullname + ", UserType: " + userType);
        
        
        return "redirect:/admin/login?registered=true";
    }
    
    @GetMapping("/admin/contracts")
    public String listContracts(Model model) {
        List<Contracts> contracts = contractService.getAllContracts();
        model.addAttribute("contracts", contracts);
        return "admin/pages/ad_contracts/"; 
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
        return "admin/pages/ad_customers"; 
    }

    @GetMapping("/customers/{customerId}")
    public String getCustomerContracts(@PathVariable int customerId, Model model) {
        List<Contracts> contracts = customerService.getCustomerContracts(customerId);
        if (contracts == null) {
            model.addAttribute("errorMessage", "No contracts found for this customer.");
            return "error"; 
        }
        
        model.addAttribute("contracts", contracts);
        return "admin/pages/ad_contracts_by_cus"; 
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
        
        return "admin/pages/ad_contract_detail"; 

    }

    @GetMapping("/contracts/{contractId}/employee")
    public Employees getEmployeeByContract(@PathVariable int contractId) {
        return customerService.getEmployeeByContract(contractId);
    }

   


    @GetMapping("/contracts")
    public String getAllContracts(Model model) {
        List<Contracts> contracts = contractService.getAllContracts();
        model.addAttribute("contracts", contracts);
        return "admin/pages/ad_contracts"; 
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
