package net.codejava.controller.Employees;

import net.codejava.helper.EmployeeValidation;
import net.codejava.model.EmployeeServices;
import net.codejava.model.Employees;
import net.codejava.model.Services;
import net.codejava.model.SalaryProposals;
import net.codejava.service.EmailService;
import net.codejava.service.EmployeeService;
import net.codejava.service.EmployeeServicesService;
import net.codejava.service.SalaryProposalService;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/employees")
public class EmployeesController {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeesController.class);
    
    @Autowired
    private EmployeeService employeeService;
    
    
    @Autowired
    private ServiceService serviceService;
    
    @Autowired
    private EmployeeServicesService employeeServicesService;
    
    @Autowired
    private SalaryProposalService salaryProposalService;
    
    private static final String UPLOAD_DIR = "uploads/";
    
    
    
    @GetMapping("/updateInfo")
    public String showUpdateInfoForm(HttpSession session, Model model) {
        Employees employee = (Employees) session.getAttribute("employee");
        
        if (employee == null) {
            return "redirect:/employees/login";
        }
        List<Services> services = serviceService.getAllServices();
        model.addAttribute("services", services);
        List<EmployeeServices> employeeServices = employeeServicesService.findAll();
        model.addAttribute("employeeServices", employeeServices);
        model.addAttribute("employee", employee);  
        List<SalaryProposals> proposals = salaryProposalService.listAllProposals();
        model.addAttribute("proposals", proposals);  
        return "employees/emp_update_info";
    }


    
    @PostMapping("/updateInfo")
    public String updateProfile(@RequestParam("fullname") String fullname,
                                @RequestParam("phone") String phone,
                                @RequestParam("address") String address,
                                @RequestParam("profileImage") MultipartFile profileImage,
                                @RequestParam("birthday") LocalDate birthday,
                                @RequestParam("bio") String bio,
                                HttpServletRequest request, Model model) {
    	
        if (!EmployeeValidation.isFullnameValid(fullname)) {
            model.addAttribute("error", "Fullname is invalid. It should be 2-50 alphabetic characters.");
            return "employees/emp_update_info";
        }

        if (!EmployeeValidation.isPhoneValid(phone)) {
            model.addAttribute("error", "Phone number is invalid. It should be 10 digits.");
            return "employees/emp_update_info";
        }

        if (!EmployeeValidation.isAddressValid(address)) {
            model.addAttribute("error", "Address is invalid. It should be between 1 and 100 characters.");
            return "employees/emp_update_info";
        }

        if (!EmployeeValidation.isBioValid(bio)) {
            model.addAttribute("error", "Bio must be less than 255 characters.");
            return "employees/emp_update_info";
        }
    	
        HttpSession session = request.getSession();
        Employees employee = (Employees) session.getAttribute("employee");

        if (employee == null) {
            return "redirect:/employees/login";
        }

        // Ensure the upload directory exists
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); 
        }

        // Update employee details
        employee.setFullname(fullname);
        employee.setPhone(phone);
        employee.setAddress(address);

        // Handle profile image upload
        if (!profileImage.isEmpty()) {
            try {
                String existingImageName = employee.getProfileImage(); 
                if (existingImageName != null && !existingImageName.isEmpty()) {
                    File existingImageFile = new File(UPLOAD_DIR + existingImageName);
                    if (existingImageFile.exists()) {
                        boolean deleted = existingImageFile.delete();
                        if (!deleted) {
                            model.addAttribute("error", "Failed to delete old image.");
                            return "employees/emp_update_info"; 
                        }
                    }
                }

                // Construct the path to save the uploaded file
                String newFileName = profileImage.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR + newFileName);

                // Write the new file to the specified path
                Files.write(uploadPath, profileImage.getBytes());

                // Save only the new filename in the database
                employee.setProfileImage(newFileName); 
            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload image.");
                return "employees/emp_update_info"; 
            }
        }
        employee.setBirthday(birthday);
        employee.setBio(bio);
        
        // Update the employee information in the database
        employeeService.updateEmployeeInfo(employee);
        
        // Update session
        session.setAttribute("employee", employee);
        return "redirect:/employees/updateInfo?success=update"; 
    }
    

    
   
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        
        model.addAttribute("products", employeeService.getAllEmployees());
        Employees employee = (Employees) session.getAttribute("employee");
        session.setAttribute("employeeId", employee.getEmployeeId());
        model.addAttribute("employee", employee);
        model.addAttribute("session", session);
        return "employees/emp_index";
    }
    
   
    @GetMapping("/login")
    public String loginForm() {
        return "employees/emp_login";
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
                return "redirect:/employees/login?unverified=true";
            }
            
            if (employeeService.checkPassword(password, employee.getPassword())) {
           
                HttpSession session = request.getSession();
                session.setAttribute("employee", employee);
                session.setAttribute("employeeId", employee.getEmployeeId());
                logger.info("User authenticated: " + email + ", UserType: " + employee.getUserType());
                
             
                switch (employee.getUserType().toUpperCase()) {
                    case "ADMIN":
                        return "redirect:/admin/dashboard";
                    case "MANAGER":
                        return "redirect:/manager/dashboard";
                    case "EMPLOYEE":
                        return "redirect:/employees/dashboard";
                    default:
                        logger.warn("Unknown userType: " + employee.getUserType());
                        return "redirect:/employees/login?error=true";
                }
            } else {
                logger.error("Password check failed for user: " + email);
            }
        } else {
            logger.error("User not found: " + email);
        }
        
        return "redirect:/employees/login?error=true";
    }

  /*   
   @GetMapping("/register") 
    public String registerForm() {
        return "employees/emp_register";
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
        
        return "redirect:/employees/login?registered=true";
    }
*/
    // Verify account
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("code") String code, Model model) {
        Employees employee = employeeService.findByVerifyCode(code);
        
        if (employee == null) {
            model.addAttribute("message", "Invalid verification code.");
            return "employees/emp_verify"; 
        }
        
        // Activate user
        employee.setStatus(1);
        employee.setVerifyCode(null); 
        employeeService.updateUser(employee);
        
        model.addAttribute("message", "Your account has been verified. You can now log in.");
        return "employees/emp_verify";
    }
    
    @GetMapping("/change-password")
    public String changePasswordForm(Model model, HttpSession session) {
        Employees employee = (Employees) session.getAttribute("employee");
        if (employee == null) {
            return "redirect:/employees/login"; // Redirect to login if not authenticated
        }
        model.addAttribute("employee", employee);
        return "employees/emp_change_password"; // Create this view
    }

    // Handle change password form submission
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session, Model model) {
        Employees employee = (Employees) session.getAttribute("employee");
        
        if (employee == null) {
            return "redirect:/employees/login";
        }

        // Check if the new passwords match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match.");
            return "redirect:/employees/updateInfo";
        }

        // Try to change the password
        boolean success = employeeService.changePassword(employee, currentPassword, newPassword);

        if (success) {
            model.addAttribute("message", "Password changed successfully.");
            return "redirect:/employees/updateInfo";// You can redirect to dashboard if needed
        } else {
            model.addAttribute("error", "Current password is incorrect.");
            return "redirect:/employees/updateInfo";
        }
    }
    
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "employees/emp_forgot_password"; // Create this view
    }

    // Handle forgot password submission
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, HttpServletRequest request) {
        String siteURL = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        employeeService.generateResetToken(email, siteURL);
        
        return "redirect:/employees/forgot-password?sent=true"; // Redirect to confirm the email was sent
    }

    // Show reset password form
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Employees employee = employeeService.findByResetToken(token);

        if (employee == null) {
            model.addAttribute("message", "Invalid or expired reset token.");
            return "employees/emp_reset_password"; // Create this view
        }

        model.addAttribute("token", token); // Pass the token to the form
        return "employees/emp_reset_password";
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
            return "employees/emp_reset_password";
        }

        boolean success = employeeService.resetPassword(token, newPassword);

        if (success) {
            model.addAttribute("message", "Your password has been reset. You can now log in.");
        } else {
            model.addAttribute("message", "Invalid or expired reset token.");
        }

        return "employees/emp_reset_password";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
    
}