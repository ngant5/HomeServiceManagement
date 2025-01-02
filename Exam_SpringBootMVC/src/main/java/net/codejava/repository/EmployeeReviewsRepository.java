package net.codejava.repository;

import net.codejava.model.EmployeeReviews;
import net.codejava.model.ContractDetails;
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
        String sql = "SELECT * FROM Contract_Details WHERE contract_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ContractDetails.class), contractId);
    }

    // Lấy tất cả các đánh giá nhân viên theo contractDetailId
    public List<EmployeeReviews> findReviewsByContractDetailId(int contractDetailId) {
        String sql = "SELECT * FROM Employee_Reviews WHERE contract_detail_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EmployeeReviews.class), contractDetailId);
    }

    // Lấy tất cả emp_service_id theo contractId
    public List<Integer> findEmpServiceIdsByContractId(int contractId) {
        String sql = "SELECT emp_service_id FROM Contract_Details WHERE contract_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, contractId);
    }
}
