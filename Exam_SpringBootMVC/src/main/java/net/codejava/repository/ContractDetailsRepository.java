package net.codejava.repository;

import net.codejava.controller.ContractController;
import net.codejava.model.ContractDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ContractDetailsRepository {

    private final JdbcTemplate jdbcTemplate;
    
    private static final Logger logger = LoggerFactory.getLogger(ContractDetailsRepository.class);

    public ContractDetailsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean empServiceIdExists(int empServiceId) {
        String sql = "SELECT COUNT(*) FROM Employee_Services WHERE emp_service_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, empServiceId);
        return count != null && count > 0;
    }

    
    
    // Tạo chi tiết hợp đồng đơn lẻ
    public void createContractDetail(ContractDetails contractDetail) {
    	if (!empServiceIdExists(contractDetail.getEmpServiceId())) {
            throw new IllegalArgumentException("empServiceId không hợp lệ: " + contractDetail.getEmpServiceId());
        }
    	
        String sql = "INSERT INTO Contract_Details (contract_id, emp_service_id, employee_id, service_address, service_phone, start_date, end_date, status, hours_worked, total_price, contract_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"+
                     "SELECT SCOPE_IDENTITY();"; 
        logger.info("Trying to insert contract detail with emp_service_id: {}", contractDetail.getEmpServiceId());

        Integer generatedId = jdbcTemplate.queryForObject(sql, Integer.class,
                contractDetail.getContractId(),
                contractDetail.getEmpServiceId(),
                contractDetail.getEmployeeId(),
                contractDetail.getServiceAddress(),
                contractDetail.getServicePhone(),
                contractDetail.getStartDate(),
                contractDetail.getEndDate(),
                contractDetail.getStatus(),
                contractDetail.getHoursWorked(),
                contractDetail.getTotalPrice(),
                contractDetail.getContractType());

        if (generatedId != null) {
            contractDetail.setContractDetailId(generatedId);  
            logger.info("Inserted contract detail with contractDetailId: {}", contractDetail.getContractDetailId());
        } else {
            logger.error("Failed to retrieve contractDetailId after insert.");
        }
    }


    // Tạo danh sách chi tiết hợp đồng
    public void createContractDetails(List<ContractDetails> contractDetails) {
        for (ContractDetails detail : contractDetails) {
            createContractDetail(detail); // Gọi phương thức tạo đơn lẻ
        }
    }

    // Lấy tất cả chi tiết hợp đồng
    public List<ContractDetails> getAllContractDetails() {
        String sql = "SELECT * FROM Contract_Details";
        return jdbcTemplate.query(sql, this::mapRowToContractDetail);
    }

    
    public ContractDetails getContractDetailById(int id) {
        String sql = "SELECT * FROM Contract_Details WHERE contract_detail_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToContractDetail, id);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No contract details found for contract_detail_id: {}", id);
            return null; 
        }
    }

    public void updateContractDetail(ContractDetails contractDetail) {
        // SQL để cập nhật thông tin chi tiết hợp đồng
        String sql = "UPDATE Contract_Details SET emp_service_id = ?, employee_id = ?, service_phone = ?, service_address = ?, " +
                     "start_date = ?, end_date = ?, status = ?, hours_worked = ?, total_price = ?, contract_type = ? " +
                     "WHERE contract_detail_id = ?";
        
        // Thực hiện cập nhật bản ghi vào cơ sở dữ liệu
        jdbcTemplate.update(sql, 
                            contractDetail.getEmpServiceId(),
                            contractDetail.getEmployeeId(),
                            contractDetail.getServicePhone(),
                            contractDetail.getServiceAddress(),
                            contractDetail.getStartDate(),
                            contractDetail.getEndDate(),
                            contractDetail.getStatus(),
                            contractDetail.getHoursWorked(),
                            contractDetail.getTotalPrice(),
                            contractDetail.getContractDetailId());
    }

    public void deleteContractDetail(int id) {
        String sql = "DELETE FROM Contract_Details WHERE contract_detail_id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    public List<ContractDetails> findByContractId(int contractId) {
        String sql = "SELECT * FROM Contract_Details WHERE contract_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToContractDetail, contractId);
    }


    private ContractDetails mapRowToContractDetail(ResultSet rs, int rowNum) throws SQLException {
        ContractDetails detail = new ContractDetails();
        detail.setContractDetailId(rs.getInt("contract_detail_id"));
        detail.setContractId(rs.getInt("contract_id"));
        detail.setEmpServiceId(rs.getInt("emp_service_id"));
        detail.setEmployeeId(rs.getInt("employee_id"));
        detail.setServiceAddress(rs.getString("service_address"));
        detail.setServicePhone(rs.getString("service_phone"));
        detail.setStartDate(rs.getTimestamp("start_date").toLocalDateTime().toLocalDate());
        detail.setEndDate(rs.getTimestamp("end_date").toLocalDateTime().toLocalDate());
        detail.setStatus(rs.getInt("status"));
        detail.setHoursWorked(rs.getString("hours_worked"));
        detail.setTotalPrice(rs.getString("total_price"));
        return detail;
    }
    

    public Optional<ContractDetails> findById(Long id) {
        String sql = "SELECT * FROM Contract_Details WHERE contract_detail_id = ?";
        try {
            ContractDetails contractDetail = jdbcTemplate.queryForObject(sql, this::mapRowToContractDetail, id);
            return Optional.ofNullable(contractDetail);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No contract details found for contract_detail_id: {}", id);
            return Optional.empty();
        }
    }

}
