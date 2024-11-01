package net.codejava.controller;

import net.codejava.model.Customers;
import net.codejava.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/customer/profile")
public class CustomerProfileController {

    @Autowired
    private CustomerService customerService;

    private static final String UPLOAD_DIR = "uploads/";

    private Customers getCustomerFromSession(HttpSession session) {
        return (Customers) session.getAttribute("customer");
    }

    @GetMapping("/cus_mypage")
    public String viewMyPage(Model model, HttpSession session) {
        Customers customer = getCustomerFromSession(session);
        if (customer == null) {
            return "redirect:/customer/auth/login";
        }
        model.addAttribute("customer", customer);
        return "customer/cus_mypage_";
    }

    @GetMapping("/info")
    public String getCustomerInfo(HttpServletRequest request, Model model) {
        Customers customer = getCustomerFromSession(request.getSession());
        if (customer != null) {
            model.addAttribute("customerInfo", customerService.getCustomerInfo(customer.getCustomerId()));
            return "customer/cus_info";
        }
        return "redirect:/customer/auth/login";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam("fullname") String fullname,
                                @RequestParam("phone") String phone,
                                @RequestParam("address") String address,
                                @RequestParam("profileImage") MultipartFile profileImage,
                                HttpServletRequest request, Model model) {
        Customers customer = getCustomerFromSession(request.getSession());

        if (customer == null) {
            return "redirect:/customer/auth/login";
        }

        customer.setFullname(fullname);
        customer.setPhone(phone);
        customer.setAddress(address);

        // Xử lý hình ảnh đại diện
        if (!profileImage.isEmpty()) {
            // Tạo thư mục nếu chưa tồn tại
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String newFileName = profileImage.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR + newFileName);
            try {
                Files.write(uploadPath, profileImage.getBytes());
                customer.setProfileImage(newFileName);
            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload image.");
                return "customer/cus_info";
            }
        }

        customerService.updateCustomerInfo(customer);
        request.getSession().setAttribute("customer", customer);
        return "redirect:/customer/index";
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Model model, HttpSession session) {
        if (getCustomerFromSession(session) == null) {
            return "redirect:/customer/auth/login";
        }
        return "customer/cus_change_password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session, Model model) {
        Customers customer = getCustomerFromSession(session);

        if (customer == null) {
            return "redirect:/customer/auth/login";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match.");
            return "customer/cus_change_password";
        }

        boolean success = customerService.changePassword(customer, currentPassword, newPassword);

        if (success) {
            model.addAttribute("message", "Password changed successfully.");
            return "redirect:/customer/index";
        } else {
            model.addAttribute("error", "Current password is incorrect.");
            return "customer/cus_change_password";
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "customer/cus_forgot_password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, HttpServletRequest request) {
        customerService.generateResetToken(email, request.getRequestURL().toString());
        return "redirect:/customer/forgot-password?sent=true";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        if (customerService.findByResetToken(token) == null) {
            model.addAttribute("message", "Invalid or expired reset token.");
            return "customer/cus_reset_password";
        }
        model.addAttribute("token", token);
        return "customer/cus_reset_password";
    }

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

        if (customerService.resetPassword(token, newPassword)) {
            model.addAttribute("message", "Your password has been reset. You can now log in.");
        } else {
            model.addAttribute("message", "Invalid or expired reset token.");
        }

        return "customer/cus_reset_password";
    }
}
