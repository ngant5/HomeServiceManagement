package net.codejava.service;

import net.codejava.model.Contracts;
import net.codejava.model.Customers;
import net.codejava.model.Employees;
import net.codejava.model.Payments;
import net.codejava.repository.CustomerRepository;
import net.codejava.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
    
    @Autowired
    private EmployeeRepository employeeRepository;


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
        return customerRepository.findByVerifyCode(verifyCode).orElse(null); 
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
    
    public Customers getCustomerInfo(int customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    public boolean updateCustomerInfo(Customers customer) {
        return customerRepository.updateUser(customer) > 0;
    }
    
    public List<Customers> getAllCustomers() {
        return customerRepository.findAll(); 
    }
    
    public List<Contracts> getCustomerContracts(int customerId) {
        return customerRepository.findContractsByCustomerId(customerId); 
    }
    
    public List<Payments> getPaymentsByContract(int contractId) {
        return customerRepository.findPaymentsByContractId(contractId); 
    }
    
    public void sendPaymentReminder(int contractId) {
    }

    public void setPaymentDueDate(int contractId, LocalDateTime dueDate) {
        
        jdbcTemplate.update("UPDATE Contracts SET due_date=? WHERE contract_id=?", dueDate, contractId);
    }
    
    public void updateContractEmployee(int contractId, int employeeId) {
        
        jdbcTemplate.update("UPDATE Contracts SET employee_id=? WHERE contract_id=?", employeeId, contractId);
    }

    public Employees getEmployeeByContract(int contractId) {
        return employeeRepository.findByContractId(contractId);
    }

    public void updateContract(int contractId, Contracts contract) {
        String sql = "UPDATE Contracts SET customer_id=?, contract_status=?, total_price=?, payment_status=?, created_at=?, contract_file=? WHERE contract_id=?";
        jdbcTemplate.update(sql, 
            contract.getCustomerId(), 
            contract.getContractStatus(), 
            contract.getTotalPrice(),    
            contract.getPaymentStatus(), 
            contract.getCreatedAt(),     
            contract.getContractFile(),   
            contractId                   
        );
    }


    
    public Contracts getContractById(int contractId) {
        String sql = "SELECT * FROM Contracts WHERE contract_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{contractId}, new BeanPropertyRowMapper<>(Contracts.class));
    }

    
    public List<Contracts> getAllContracts() {
        String sql = "SELECT * FROM Contracts";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Contracts.class));
    }

    
    public List<Contracts> findContractsByDuration(int duration) {
        String sql = "SELECT * FROM Contracts WHERE duration = ?";
        return jdbcTemplate.query(sql, new Object[]{duration}, new BeanPropertyRowMapper<>(Contracts.class));
    }

    

}