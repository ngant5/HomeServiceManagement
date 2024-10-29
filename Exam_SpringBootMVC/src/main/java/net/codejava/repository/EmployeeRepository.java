package net.codejava.repository;

import net.codejava.model.Employees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Check if email exists
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Employees WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        return count != null && count > 0;
    }

    // Find by ID
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
    
    

    // Find by user type
    public List<Employees> findByUserType(String userType) {
        String sql = "SELECT * FROM Employees WHERE user_type = ?";
        return jdbcTemplate.query(
            sql,
            new Object[]{userType},
            new BeanPropertyRowMapper<>(Employees.class)
        );
    }

    // Find by username (assuming 'fullname' is used as username)
    public Optional<Employees> findByUserName(String userName) {
        String sql = "SELECT * FROM Employees WHERE fullname = ?";
        try {
            Employees employee = jdbcTemplate.queryForObject(
                sql,
                new Object[]{userName},
                new BeanPropertyRowMapper<>(Employees.class)
            );
            return Optional.of(employee);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Find by email
    public Optional<Employees> findByEmail(String email) {
        String sql = "SELECT * FROM Employees WHERE email = ?";
        try {
            Employees employee = jdbcTemplate.queryForObject(
                sql,
                new Object[]{email},
                new BeanPropertyRowMapper<>(Employees.class)
            );
            return Optional.of(employee);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Find by verification code
    public Optional<Employees> findByVerifyCode(String verifyCode) {
        String sql = "SELECT * FROM Employees WHERE verify_code = ?";
        try {
            Employees employee = jdbcTemplate.queryForObject(
                sql,
                new Object[]{verifyCode},
                new BeanPropertyRowMapper<>(Employees.class)
            );
            return Optional.of(employee);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Save a new employee
    public int saveUser(Employees employee) {
        String sql = "INSERT INTO Employees (fullname, password, user_type, email, phone, address, profile_image, experience_years, salary, status, verify_code, token, created_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            employee.getCreatedAt()
        );
    }

    // Update employee information
    public int updateUser(Employees employee) {
        String sql = "UPDATE Employees SET fullname=?, password=?, user_type=?, email=?, phone=?, address=?, profile_image=?, experience_years=?, salary=?, status=?, verify_code=?, token=?, created_at=? WHERE employee_id=?";
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
            employee.getEmployeeId()
        );
    }
    
    public List<Employees> findEmployeesByServiceId(int serviceId) {
        String sql = "SELECT e.* FROM Employees e " +
                     "JOIN emp_service es ON e.employee_id = es.employee_id " +
                     "WHERE es.service_id = ?";
        
        return jdbcTemplate.query(sql, new Object[]{serviceId}, new BeanPropertyRowMapper<>(Employees.class));
    }
    
    public Optional<Employees> findByToken(String token) {
        String sql = "SELECT * FROM Employees WHERE token = ?";
        
        List<Employees> employees = jdbcTemplate.query(sql, new Object[]{token}, new RowMapper<Employees>() {
            @Override
            public Employees mapRow(ResultSet rs, int rowNum) throws SQLException {
                Employees employee = new Employees();
                employee.setEmployeeId(rs.getInt("employee_id"));
                employee.setFullname(rs.getString("fullname"));
                employee.setPassword(rs.getString("password"));
                employee.setUserType(rs.getString("user_type"));
                employee.setEmail(rs.getString("email"));
                employee.setPhone(rs.getString("phone"));
                employee.setAddress(rs.getString("address"));
                employee.setProfileImage(rs.getString("profile_image"));
                employee.setExperienceYears(rs.getInt("experience_years"));
                employee.setSalary(rs.getInt("salary"));
                employee.setStatus(rs.getInt("status"));
                employee.setVerifyCode(rs.getString("verify_code"));
                employee.setToken(rs.getString("token"));
                employee.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return employee;
            }
        });

        if (employees.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(employees.get(0));
    }

    // Delete employee by ID
    public int deleteById(int id) {
        String sql = "DELETE FROM Employees WHERE employee_id = ?";
        return jdbcTemplate.update(sql, id);
    }
    
    public Employees findByContractId(int contractId) {
        String sql = "SELECT e.* FROM Employees e " +
                     "JOIN Contracts c ON e.employee_id = c.employee_id " +
                     "WHERE c.contract_id = ?";
        return jdbcTemplate.queryForObject(
            sql,
            new Object[]{contractId},
            new BeanPropertyRowMapper<>(Employees.class)
        );
    }

}