package net.codejava.repository;

import net.codejava.model.EmployeeReviews;
import net.codejava.model.ContractDetails;
import net.codejava.model.EmployeeInfo;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeReviewsRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeReviewsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Lấy tất cả các ContractDetails theo contractId
    public List<ContractDetails> findContractDetailsByContractId(int contractId) {
    	String sql = "SELECT contract_detail_id, emp_service_id FROM Contract_Details WHERE contract_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ContractDetails.class), contractId);
    }

    // Lấy tất cả các đánh giá nhân viên theo contractDetailId
    public List<EmployeeReviews> findReviewsByContractDetailId(int contractDetailId) {
        String sql = "SELECT * FROM Employee_Reviews WHERE contract_detail_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EmployeeReviews.class), contractDetailId);
    }
    
    public List<EmployeeInfo> findEmployeeInfoByServiceId(int serviceId) {
        String sql = "SELECT e.id AS employeeId, e.name AS employeeName " +
                     "FROM Employees e JOIN Service s ON e.service_id = s.id " +
                     "WHERE s.id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new EmployeeInfo(
            rs.getInt("employeeId"),
            rs.getString("employeeName")
        ), serviceId);
    }


    // Lấy tất cả emp_service_id theo contractId
    public List<Integer> findEmpServiceIdsByContractId(int contractId) {
        String sql = "SELECT emp_service_id FROM Contract_Details WHERE contract_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, contractId);
    }
    
    public boolean existsByContractDetailIdAndEmployeeId(int contractDetailId, int employeeId) {
        String sql = "SELECT COUNT(*) FROM Employee_Reviews WHERE contract_detail_id = ? AND employee_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, contractDetailId, employeeId);
        return count != null && count > 0;
    }

    // Lưu review mới vào cơ sở dữ liệu
    public void saveReview(EmployeeReviews review) {
        String sql = "INSERT INTO Employee_Reviews (contract_detail_id, employee_id, rating, comment, created_at) " +
                     "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, review.getContractDetailId(), review.getEmployeeId(), review.getRating(),
                            review.getComment(), review.getCreatedAt());
    }

    // Cập nhật review nếu đã tồn tại
    public void updateReview(EmployeeReviews review) {
        String sql = "UPDATE Employee_Reviews SET rating = ?, comment = ?, created_at = ? " +
                     "WHERE contract_detail_id = ? AND employee_id = ?";
        jdbcTemplate.update(sql, review.getRating(), review.getComment(), review.getCreatedAt(),
                            review.getContractDetailId(), review.getEmployeeId());
    }
    
    public List<EmployeeReviews> findAll() {
        String sql = "SELECT * FROM Employee_Reviews";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EmployeeReviews.class));
    }

}