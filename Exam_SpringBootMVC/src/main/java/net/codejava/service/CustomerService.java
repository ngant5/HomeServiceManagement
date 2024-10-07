package net.codejava.service;

import net.codejava.model.Customers;
import net.codejava.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private JdbcTemplate jdbcTemplate; 


    public Customers findById(int id) {
        return customerRepository.findById(id).orElse(null); 
    }
    
    public Customers findByEmail(String email) {
        return customerRepository.findByEmail(email).orElse(null); 
    }
   
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

    public Customers saveUser(Customers customer, String siteURL) {
        
        String hashedPassword = BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt());
        customer.setPassword(hashedPassword);
        
        
        if (customer.getCreatedAt() == null) {
        	customer.setCreatedAt(LocalDateTime.now());
        }
        
        
        customer.setStatus(0);
        
        
        String verifyCode = UUID.randomUUID().toString();
        customer.setVerifyCode(verifyCode);
        
        
        customerRepository.saveUser(customer);
        
        
        emailService.sendVerificationEmailforCustomer(customer, siteURL);
        
        return customer;
    }
    
    public Customers findByVerifyCode(String verifyCode) {
        return customerRepository.findByVerifyCode(verifyCode).orElse(null); // Adjust repository method if needed
    }

   
    public int updateUser(Customers customer) {
        return jdbcTemplate.update(
            "UPDATE Customers SET fullname=?, password=?, email=?, phone=?, address=?, profile_image=?, status=?, created_at=?, verify_code=?, token=?  WHERE customer_id=?",
            customer.getFullname(),
            customer.getPassword(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getAddress(),
            customer.getProfileImage(),
            customer.getStatus(),
            customer.getCreatedAt(),
            customer.getVerifyCode(),
            customer.getToken(),
            customer.getCustomerId()
        );
    }

   
    public boolean deleteById(int id) {
        return customerRepository.deleteById(id) > 0;
    }
}