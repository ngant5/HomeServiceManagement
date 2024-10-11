package net.codejava.service;

import net.codejava.model.Employees;
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
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private JdbcTemplate jdbcTemplate; 
    
    public List<Employees> getAllEmployees(){
    	String sql = "SELECT * FROM Employees";
    	return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Employees.class));
    }
    
    public boolean changePassword(Employees employee, String currentPassword, String newPassword) {
        // Verify current password
        if (checkPassword(currentPassword, employee.getPassword())) {
            // Encrypt and set new password
            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            employee.setPassword(hashedNewPassword);
            
            // Update employee in the database
            int updated = updateUser(employee); 
            return updated > 0; // Return true if the update was successful
        } 
        return false; // Return false if the current password is incorrect
    }
    
    public void generateResetToken(String email, String siteURL) {
        Employees employee = findByEmail(email);
        
        if (employee != null) {
            // Generate reset token
            String token = UUID.randomUUID().toString();
            employee.setToken(token);
            updateUser(employee); // Save the token to the database
            
            // Send reset email
            String resetURL = siteURL + "/employees/reset-password?token=" + token;
            emailService.sendResetPasswordEmailforEmployees(employee, resetURL);
        }
    }

    // Find user by reset token
    public Employees findByResetToken(String token) {
        return employeeRepository.findByToken(token).orElse(null);
    }

    // Reset password
    public boolean resetPassword(String token, String newPassword) {
        Employees employee = findByResetToken(token);
        
        if (employee != null) {
            // Encrypt the new password
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            employee.setPassword(hashedPassword);
            employee.setToken(null); // Clear the token after successful reset
            updateUser(employee);
            return true;
        }
        return false;
    }


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
    
    public int updateEmployeeInfo(Employees employee) {
    	return jdbcTemplate.update(
    			"UPDATE Employees SET fullname=?, phone=?, address=?, profile_image=? WHERE employee_id=?",
    			employee.getFullname(),
    			employee.getPhone(),
    			employee.getAddress(),
    			employee.getProfileImage(),
    			employee.getEmployeeId()
    			);
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