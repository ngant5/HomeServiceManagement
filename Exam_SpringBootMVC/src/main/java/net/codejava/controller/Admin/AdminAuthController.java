package net.codejava.controller.Admin;

import net.codejava.model.Employees;
import net.codejava.service.EmailService;
import net.codejava.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {
    
    private final EmployeeService employeeService;
    private final EmailService emailService;

    @Autowired
    public AdminAuthController(EmployeeService employeeService, EmailService emailService) {
        this.employeeService = employeeService;
        this.emailService = emailService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "admin/ad_login";
    }

    @PostMapping("/chklogin")
    public String checkLogin(@RequestParam("email") String email,
                             @RequestParam("pwd") String password,
                             HttpServletRequest request) {
        try {
            Employees employee = employeeService.findByEmail(email);
            if (employee == null) {
                return "redirect:/admin/login?error=userNotFound";
            }
            if (employee.getStatus() == 0) {
                return "redirect:/admin/login?error=unverified";
            }
            if (!employeeService.checkPassword(password, employee.getPassword())) {
                return "redirect:/admin/login?error=invalidPassword";
            }
            
            request.getSession().setAttribute("employee", employee);
            return employee.getUserType().equalsIgnoreCase("ADMIN") ? "redirect:/admin/dashboard" : "redirect:/employee/dashboard";
        } catch (Exception e) {
            return "redirect:/admin/login?error=serverError";
        }
    }

    @GetMapping("/change-password")
    public String changePasswordForm(HttpSession session, Model model) {
        Employees employee = (Employees) session.getAttribute("employee");
        if (employee == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("employee", employee);
        return "admin/pages/ad_change_password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session, Model model) {
        Employees employee = (Employees) session.getAttribute("employee");
        
        if (employee == null) {
            return "redirect:/admin/login";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match.");
            return "admin/ad_change_password";
        }

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
        
        return "redirect:/admin/forgot-password?sent=true";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Employees employee = employeeService.findByResetToken(token);

        if (employee == null) {
            model.addAttribute("message", "Invalid or expired reset token.");
            return "admin/ad_reset_password";
        }

        model.addAttribute("token", token);
        return "admin/ad_reset_password";
    }

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
        model.addAttribute("message", success ? "Your password has been reset. You can now log in." : "Invalid or expired reset token.");
        return "admin/ad_reset_password";
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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
