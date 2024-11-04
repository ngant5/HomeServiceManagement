package net.codejava.repository;

import net.codejava.model.EmployeeServices;
import net.codejava.model.Employees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class EmployeeServicesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServices.class);

    // RowMapper to map database rows to EmployeeServices model
    private RowMapper<EmployeeServices> rowMapper = new RowMapper<EmployeeServices>() {
        @Override
        public EmployeeServices mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeServices employeeService = new EmployeeServices();
            employeeService.setEmpServiceId(rs.getInt("emp_service_id"));
            employeeService.setEmployeeId(rs.getInt("employee_id"));
            employeeService.setServiceId(rs.getInt("service_id"));
            employeeService.setDetails(rs.getString("details"));
            return employeeService;
        }
    };

    public List<EmployeeServices> findByEmployeeId(int employeeId) {
    	logger.info("Executing query to find services for employee ID: {}", employeeId);
        String sql = "SELECT * FROM Employee_Services WHERE employee_id = ?";
        return jdbcTemplate.query(sql, new Object[]{employeeId}, rowMapper);
    }


    

    // RowMapper to map database rows to Employees model
    private RowMapper<Employees> employeeRowMapper = new RowMapper<Employees>() {
        @Override
        public Employees mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employees employee = new Employees();
            employee.setEmployeeId(rs.getInt("employee_id"));
            employee.setFullname(rs.getString("fullname"));
            employee.setProfileImage(rs.getString("profile_image"));
            employee.setExperienceYears(rs.getInt("experience_years"));
            return employee;
        }
    };

    // Create
    public void save(EmployeeServices employeeService) {
        String sql = "INSERT INTO Employee_Services (employee_id, service_id, details) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, employeeService.getEmployeeId(), employeeService.getServiceId(), employeeService.getDetails());
    }

    // Read by ID
    public EmployeeServices findById(int emp_service_id) {
        String sql = "SELECT * FROM Employee_Services WHERE emp_service_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{emp_service_id}, rowMapper);
    }

    // Read all
    public List<EmployeeServices> findAll() {
        String sql = "SELECT * FROM Employee_Services";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // Update
    public void update(EmployeeServices employeeService) {
        String sql = "UPDATE Employee_Services SET employee_id = ?, service_id = ?, details = ? WHERE emp_service_id = ?";
        jdbcTemplate.update(sql, employeeService.getEmployeeId(), employeeService.getServiceId(), employeeService.getDetails(), employeeService.getEmpServiceId());
    }

    // Delete
    public void deleteById(int emp_service_id) {
        String sql = "DELETE FROM Employee_Services WHERE emp_service_id = ?";
        jdbcTemplate.update(sql, emp_service_id);
    }

    // Phương thức mới để tìm nhân viên theo service_id
    public List<EmployeeServices> findByServiceId(int serviceId) {
        String sql = "SELECT * FROM Employee_Services WHERE service_id = ?";
        return jdbcTemplate.query(sql, new Object[]{serviceId}, rowMapper);
    }
    
   
    
    public Employees findEmployeeByEmployeeId(int employeeId) {
        String sql = "SELECT fullname, profile_image, experience_years FROM Employees WHERE employee_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{employeeId}, new RowMapper<Employees>() {
            public Employees mapRow(ResultSet rs, int rowNum) throws SQLException {
                Employees employee = new Employees();
                employee.setFullname(rs.getString("fullname"));
                employee.setProfileImage(rs.getString("profile_image"));
                employee.setExperienceYears(rs.getInt("experience_years"));
                return employee;
            }
        });
    }


}