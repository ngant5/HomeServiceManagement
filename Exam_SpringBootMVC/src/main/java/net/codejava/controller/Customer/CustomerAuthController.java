package net.codejava.controller.Customer;

import net.codejava.model.Customers;
import net.codejava.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @GetMapping("/index")
    public String showHomePage(HttpSession session, Model model) {
        if (isCustomerLoggedIn(session)) {
        	Customers customer = (Customers) session.getAttribute("customer");
            
            if (customer != null) {
                model.addAttribute("customer", customer);  
            }

        }
        return "home/index";  
    }
    
    @GetMapping("/login")
    public String loginForm() {
        return "customer/cus_login";
    }

    @PostMapping("/chklogin")
    public String checkLogin(@RequestParam("email") String email,
                             @RequestParam("pwd") String password,
                             HttpServletRequest request,
                             HttpSession session,
                             Model model) {
        logger.info("Checking login for email: {}", email);
        Customers customer = customerService.findByEmail(email);

        if (customer != null) {
            if (customer.getStatus() == 0) {
                return "redirect:/customer/login?unverified=true";
            }

            if (customerService.checkPassword(password, customer.getPassword())) {
                session.setAttribute("customer", customer);
                session.setAttribute("customerId", customer.getCustomerId());
               
                logger.info("Customer logged in, session contains: {}", session.getAttribute("customer"));
                return "redirect:/index"; 
            } else {
            	model.addAttribute("error", "Wrong password!");
                return "customer/cus_login"; //
            }
        } else {
        	model.addAttribute("error", "Wrong email or password!"); 
            return "customer/cus_login"; 
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
      

        return "redirect:/index";
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
            return "redirect:/customer/register?error=emailExists";
        }

        Customers newCustomer = new Customers();
        newCustomer.setFullname(fullname);
        newCustomer.setPassword(password);
        newCustomer.setEmail(email);
        newCustomer.setPhone(phone);
        newCustomer.setAddress(address);
        newCustomer.setStatus(0); 
        String siteURL = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        customerService.saveUser(newCustomer, siteURL);
        
        return "redirect:/customer/login?registered=true";
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
        return "redirect:/customer/login";
    }
}
