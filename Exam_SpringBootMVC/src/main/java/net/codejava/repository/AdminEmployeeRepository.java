package net.codejava.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.codejava.model.EmployeeReviews;
import net.codejava.model.Employees;

@Repository
public class AdminEmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public AdminEmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public Optional<Employees> findById(int id) {
        String sql = "SELECT * FROM Employees WHERE employee_id = ?";
        try {
            Employees employee = jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                new BeanPropertyRowMapper<>(Employees.class)
            );
            return Optional.of(employee);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void updateStatus(int employeeId, int status) {
        String sql = "UPDATE Employees SET status = ? WHERE employee_id = ?";
        jdbcTemplate.update(sql, status, employeeId);
    }
    
    public void updateExperienceYears(int employeeId, int experienceYears) {
        String sql = "UPDATE Employees SET experience_years = ? WHERE employee_id = ?";
        jdbcTemplate.update(sql, experienceYears, employeeId);
    }
    
    public int save(Employees employee) {
        String sql = "INSERT INTO Employees (fullname, password, user_type, email, phone, address, profile_image, experience_years, salary, status, verify_code, token, created_at, birthday, bio) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
            sql,
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
            employee.getBirthday(),
            employee.getBio()
        );
    }
    
    public List<EmployeeReviews> findAllEmployeeReviews() {
        String sql = "SELECT * FROM Employee_Reviews";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EmployeeReviews.class));
    }
}
