package net.codejava.service;

import net.codejava.model.Employees;
import net.codejava.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private JdbcTemplate jdbcTemplate; 

    // Tìm theo ID
    public Employees findById(int id) {
        return employeeRepository.findById(id).orElse(null); // This can stay as is if your repository still returns Optional
    }
    
    public Employees findByEmail(String email) {
        return employeeRepository.findByEmail(email).orElse(null); // Adjust repository method if needed
    }

    // Tìm theo userType
    public List<Employees> findByUserType(String userType) {
        return employeeRepository.findByUserType(userType);
    }

    // Tìm theo username
    public Employees findByUserName(String userName) {
        return employeeRepository.findByUserName(userName).orElse(null); // Adjust repository method if needed
    }

    // Kiểm tra mật khẩu
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

    // Lưu nhân viên mới
    public Employees saveUser(Employees employee, String siteURL) {
        // Encrypt password
        String hashedPassword = BCrypt.hashpw(employee.getPassword(), BCrypt.gensalt());
        employee.setPassword(hashedPassword);
        
        // Set created time
        if (employee.getCreatedAt() == null) {
            employee.setCreatedAt(LocalDateTime.now());
        }
        
        // Set status to inactive
        employee.setStatus(0);
        
        // Generate verification code
        String verifyCode = UUID.randomUUID().toString();
        employee.setVerifyCode(verifyCode);
        
        // Save user
        employeeRepository.saveUser(employee);
        
        // Send verification email
        emailService.sendVerificationEmailforEmployees(employee, siteURL);
        
        return employee;
    }
    
    public Employees findByVerifyCode(String verifyCode) {
        return employeeRepository.findByVerifyCode(verifyCode).orElse(null); // Adjust repository method if needed
    }

    // Cập nhật nhân viên
    public int updateUser(Employees employee) {
        return jdbcTemplate.update(
            "UPDATE Employees SET fullname=?, password=?, user_type=?, email=?, phone=?, address=?, profile_image=?, experience_years=?, salary=?, status=?, verify_code=?, token=?, created_at=? WHERE employee_id=?",
            employee.getFullname(),
            employee.getPassword(),
            employee.getUserType(),
            employee.getEmail(),
            employee.getPhone(),
            employee.getAddress(),
            employee.getProfileImage(),
            employee.getExperienceYears(),
            employee.getSalary(),
            employee.getStatus(),
            employee.getVerifyCode(),
            employee.getToken(),
            employee.getCreatedAt(),
            employee.getEmployeeId()
        );
    }

    // Xóa nhân viên
    public boolean deleteById(int id) {
        return employeeRepository.deleteById(id) > 0;
    }
}