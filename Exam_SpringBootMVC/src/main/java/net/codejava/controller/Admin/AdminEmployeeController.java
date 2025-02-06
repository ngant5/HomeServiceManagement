package net.codejava.controller.Admin;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import net.codejava.model.Employees;
import net.codejava.model.EmployeeReviews;
import net.codejava.service.AdminEmployeeService;
import net.codejava.service.EmailService;
import net.codejava.service.EmployeeService;

@Controller
@RequestMapping("/admin/employees")
public class AdminEmployeeController {
	
	private final AdminEmployeeService adminEmployeeService;
    private final EmployeeService empService;
    private static final Logger logger = LoggerFactory.getLogger(AdminEmployeeController.class);

    @Autowired
    public AdminEmployeeController(EmployeeService empService, EmailService emailService, AdminEmployeeService adminEmployeeService) {
        this.empService = empService;
        this.adminEmployeeService = adminEmployeeService;	
    }

    @GetMapping("/list")
    public String getAllEmployees(Model model) {
    	List<Employees> employees = empService.findAllEmployees();
        model.addAttribute("employees", employees);
        return "admin/employees/emp_list"; 
    }
    
    
    @GetMapping("/addEmployee")
    public String registerForm() {
        return "admin/employees/emp_add";
    }
    
    @PostMapping("/addEmployee")
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
        try {
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

            empService.saveUser(newEmployee, siteURL);
            logger.info("New user registered: " + fullname + ", UserType: " + userType);
            return "redirect:/admin/employees/addEmployee?success=add";
        } catch (Exception e) {
            logger.error("Error during registration", e);
            return "redirect:/admin/employees/addEmployee?error=serverError";
        }
    }
    
    @PostMapping("/updateExperience/{employeeId}")
    public String updateExperience(@PathVariable("employeeId") int employeeId, @RequestParam("experienceYears") int experienceYears) {
        // Call service to update the experience years
        adminEmployeeService.updateExperienceYears(employeeId, experienceYears);
        return "redirect:/admin/employees/list"; // Redirect back to employee list
    }
    
    @PostMapping("/deleteStatus/{employeeId}")
    public String deleteEmployee(@PathVariable("employeeId") int employeeId,
    							@RequestParam("status") int status) {
        try {
        	adminEmployeeService.deleteEmployee(employeeId, status);
            return "redirect:/admin/employees/list?success=delete";
        } catch (Exception e) {
            logger.error("Error deleting employee", e);
            return "redirect:/admin/employees/list?error=delete";
        }
    }
    
    @GetMapping("/empReviews")
    public String getAllEmployeeReviews(Model model) {
    	List<Employees> employees = empService.findAllEmployees();
        model.addAttribute("employees", employees);
    	List<EmployeeReviews> empReviews = adminEmployeeService.findAllEmployeeReviews();
        model.addAttribute("empReviews", empReviews);
        return "admin/employees/emp_reviews"; 
    }

}

