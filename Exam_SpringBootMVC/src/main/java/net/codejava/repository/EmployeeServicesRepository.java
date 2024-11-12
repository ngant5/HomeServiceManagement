package net.codejava.repository;

import net.codejava.model.EmployeeServices;
import net.codejava.model.Employees;
import net.codejava.model.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class EmployeeServicesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServicesRepository.class);

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
    
    
    public List<EmployeeServices> findByEmployeeId(int employeeId) {
    	logger.info("Executing query to find services for employee ID: {}", employeeId);
        String sql = "SELECT * FROM Employee_Services WHERE employee_id = ?";
        return jdbcTemplate.query(sql, new Object[]{employeeId}, rowMapper);
    }

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

    public List<EmployeeServices> findByServiceId(int serviceId) {
        String sql = "SELECT * FROM Employee_Services WHERE service_id = ?";
        return jdbcTemplate.query(sql, new Object[]{serviceId}, rowMapper);
    }

    
    public List<EmployeeServices> findByContractDetailId(int contractDetailId) {
    	String sqlEmpService = "SELECT emp_service_id " +
                "FROM Contract_Details " +
                "WHERE contract_detail_id = ?";


    		Integer empServiceId = jdbcTemplate.queryForObject(sqlEmpService, new Object[]{contractDetailId}, Integer.class);

    			if (empServiceId == null) {
    				return Collections.emptyList(); // Trường hợp không có emp_service_id
    			}

    			String sqlServiceId = "SELECT service_id " +
                        "FROM Employee_Services " +
                        "WHERE emp_service_id = ?";
  
    			Integer serviceId = jdbcTemplate.queryForObject(sqlServiceId, new Object[]{empServiceId}, Integer.class);

    			if (serviceId == null) {
    		        logger.warn("No service_id found for emp_service_id: {}", empServiceId);
    		        return Collections.emptyList(); // Không tìm thấy service_id
    		    }

    			// Truy vấn tiếp theo để lấy tất cả nhân viên có service_id tương ứng
    			String sql = "SELECT es.emp_service_id, es.employee_id, es.service_id, es.details, e.fullname " +
    	                 "FROM Employee_Services es " +
    	                 "JOIN Employees e ON es.employee_id = e.employee_id " +
    	                 "WHERE es.service_id = ?";


        return jdbcTemplate.query(sql, new Object[]{serviceId}, (rs, rowNum) -> {
            EmployeeServices employeeServices = new EmployeeServices();
            employeeServices.setEmpServiceId(rs.getInt("emp_service_id"));
            employeeServices.setEmployeeId(rs.getInt("employee_id"));
            employeeServices.setServiceId(rs.getInt("service_id"));
            employeeServices.setDetails(rs.getString("details"));
            
            // Set thông tin nhân viên
            Employees employee = new Employees();
            employee.setEmployeeId(rs.getInt("employee_id"));
            employee.setFullname(rs.getString("fullname"));
            employeeServices.setEmployee(employee);
            
            return employeeServices;
        });
    }


    
    public Employees findEmployeeByEmployeeId(int employeeId) {
        String sql = "SELECT fullname, profile_image, experience_years FROM Employees WHERE employee_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{employeeId}, (rs, rowNum) -> {
            Employees employee = new Employees();
            employee.setFullname(rs.getString("fullname"));
            employee.setProfileImage(rs.getString("profile_image"));
            employee.setExperienceYears(rs.getInt("experience_years"));
            return employee;
        });
    }

    
    public EmployeeServices findByEmpServiceId(int empServiceId) {
        String sql = "SELECT es.emp_service_id, es.employee_id, es.service_id, es.details, " +
                     "e.fullname, e.profile_image, e.experience_years, " +
                     "s.service_name, s.service_description, s.service_price, s.service_image " +
                     "FROM Employee_Services es " +
                     "JOIN Employees e ON es.employee_id = e.employee_id " +
                     "JOIN Services s ON es.service_id = s.service_id " +
                     "WHERE es.emp_service_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{empServiceId}, (rs, rowNum) -> {
                // Tạo đối tượng EmployeeServices từ kết quả truy vấn
                EmployeeServices employeeServices = new EmployeeServices();
                employeeServices.setEmpServiceId(rs.getInt("emp_service_id"));
                employeeServices.setEmployeeId(rs.getInt("employee_id"));
                employeeServices.setServiceId(rs.getInt("service_id"));
                employeeServices.setDetails(rs.getString("details"));
                
                // Tạo đối tượng Employees và gán vào EmployeeServices
                Employees employee = new Employees();
                employee.setEmployeeId(rs.getInt("employee_id"));
                employee.setFullname(rs.getString("fullname"));
                employee.setProfileImage(rs.getString("profile_image"));
                employee.setExperienceYears(rs.getInt("experience_years"));
                employeeServices.setEmployee(employee);

                // Tạo đối tượng Services và gán vào EmployeeServices
                Services service = new Services();
                service.setServiceId(rs.getInt("service_id"));
                service.setServiceName(rs.getString("service_name"));
                service.setServiceDescription(rs.getString("service_description"));
                service.setServicePrice(rs.getInt("service_price"));
                service.setServiceImage(rs.getString("service_image"));
                employeeServices.setService(service);

                return employeeServices;
            });
        } catch (Exception e) {
            logger.error("Lỗi khi tìm kiếm EmployeeServices với emp_service_id: {}", empServiceId, e);
            return null; // Trả về null nếu có lỗi hoặc không tìm thấy
        }
    }

    public int getEmpServiceIdFromContractDetailId(int contractDetailId) {
        String sql = "SELECT emp_service_id FROM Employee_Services WHERE contract_detail_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{contractDetailId}, Integer.class);
    }

    public int getServiceIdFromEmpServiceId(int empServiceId) {
        String sql = "SELECT service_id FROM Employee_Services WHERE emp_service_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{empServiceId}, Integer.class);
    }

    public List<Employees> getEmployeesByServiceId(int serviceId) {
        String sql = "SELECT e.employee_id, e.fullname " +
                     "FROM Employee_Services es " +
                     "JOIN Employees e ON es.employee_id = e.employee_id " +
                     "WHERE es.service_id = ?";

        return jdbcTemplate.query(sql, new Object[]{serviceId}, (rs, rowNum) -> {
            Employees employee = new Employees();
            employee.setEmployeeId(rs.getInt("employee_id"));
            employee.setFullname(rs.getString("fullname"));
            return employee;
        });
    }



}