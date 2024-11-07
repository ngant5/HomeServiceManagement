package net.codejava.controller;

import net.codejava.model.Customers;
import net.codejava.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/customer")
public class CustomerAuthController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAuthController.class);

    @Autowired
    private CustomerService customerService;
    
    private boolean isCustomerLoggedIn(HttpSession session) {
        return session.getAttribute("customerId") != null;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "customer/cus_login";
    }

    @PostMapping("/chklogin")
    public String checkLogin(@RequestParam("email") String email,
                             @RequestParam("pwd") String password,
                             HttpServletRequest request,
                             HttpSession session) {
        logger.info("Checking login for email: {}", email);
        Customers customer = customerService.findByEmail(email);

        if (customer != null) {
            if (customer.getStatus() == 0) {
                return "redirect:/customer/auth/login?unverified=true";
            }

            if (customerService.checkPassword(password, customer.getPassword())) {
                session.setAttribute("customer", customer);
                session.setAttribute("customerId", customer.getCustomerId());
                return "redirect:/index";
                
            } else {
                return "redirect:/customer/auth/login?error=wrongPassword";
            }
        } else {
            return "redirect:/customer/auth/login?error=wrongPassword";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/customer/auth/login";
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
                               HttpServletRequest request) {
        if (customerService.findByEmail(email) != null) {
            return "redirect:/customer/auth/register?error=emailExists";
        }

        Customers newCustomer = new Customers();
        newCustomer.setFullname(fullname);
        newCustomer.setPassword(password);
        newCustomer.setEmail(email);
        newCustomer.setPhone(phone);
        newCustomer.setAddress(address);
        newCustomer.setStatus(0); // Chưa xác thực
        customerService.saveUser(newCustomer, request.getRequestURL().toString());
        return "redirect:/customer/auth/login?registered=true";
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("code") String code, Model model) {
        Customers customer = customerService.findByVerifyCode(code);

        if (customer != null) {
            customer.setStatus(1);
            customer.setVerifyCode(null);
            customerService.updateUser(customer);
            model.addAttribute("message", "Your account has been verified. You can now log in.");
        } else {
            model.addAttribute("message", "Invalid verification code.");
        }
        return "customer/cus_verify";
    }
}
