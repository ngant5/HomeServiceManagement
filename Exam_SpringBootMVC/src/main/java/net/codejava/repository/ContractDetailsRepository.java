package net.codejava.repository;

import net.codejava.model.ContractDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ContractDetailsRepository {

    private final JdbcTemplate jdbcTemplate;

    public ContractDetailsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Tạo chi tiết hợp đồng đơn lẻ
    public void createContractDetail(ContractDetails contractDetail) {
        String sql = "INSERT INTO ContractDetails (contract_id, emp_service_id, service_address, service_phone, start_date, end_date, status, hours_worked, total_price) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, contractDetail.getContractId(), contractDetail.getEmpServiceId(), 
                            contractDetail.getServiceAddress(), contractDetail.getServicePhone(),
                            contractDetail.getStartDate(), contractDetail.getEndDate(),
                            contractDetail.getStatus(), contractDetail.getHoursWorked(),
                            contractDetail.getTotalPrice());
    }

    // Tạo danh sách chi tiết hợp đồng
    public void createContractDetails(List<ContractDetails> contractDetails) {
        for (ContractDetails detail : contractDetails) {
            createContractDetail(detail); // Gọi phương thức tạo đơn lẻ
        }
    }

    // Lấy tất cả chi tiết hợp đồng
    public List<ContractDetails> getAllContractDetails() {
        String sql = "SELECT * FROM ContractDetails";
        return jdbcTemplate.query(sql, this::mapRowToContractDetail);
    }

    public ContractDetails getContractDetailById(int id) {
        String sql = "SELECT * FROM ContractDetails WHERE contract_detail_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToContractDetail, id);
    }

    public void updateContractDetail(ContractDetails contractDetail) {
        String sql = "UPDATE ContractDetails SET emp_service_id = ?, service_address = ?, service_phone = ?, start_date = ?, end_date = ?, status = ?, hours_worked = ?, total_price = ? WHERE contract_detail_id = ?";
        jdbcTemplate.update(sql, contractDetail.getEmpServiceId(), contractDetail.getServiceAddress(),
                            contractDetail.getServicePhone(), contractDetail.getStartDate(),
                            contractDetail.getEndDate(), contractDetail.getStatus(),
                            contractDetail.getHoursWorked(), contractDetail.getTotalPrice(), 
                            contractDetail.getContractDetailId());
    }

    public void deleteContractDetail(int id) {
        String sql = "DELETE FROM ContractDetails WHERE contract_detail_id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    public List<ContractDetails> findByContractId(int contractId) {
        String sql = "SELECT * FROM ContractDetails WHERE contract_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToContractDetail, contractId);
    }


    private ContractDetails mapRowToContractDetail(ResultSet rs, int rowNum) throws SQLException {
        ContractDetails detail = new ContractDetails();
        detail.setContractDetailId(rs.getInt("contract_detail_id"));
        detail.setContractId(rs.getInt("contract_id"));
        detail.setEmpServiceId(rs.getInt("emp_service_id"));
        detail.setServiceAddress(rs.getString("service_address"));
        detail.setServicePhone(rs.getString("service_phone"));
        detail.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
        detail.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());
        detail.setStatus(rs.getInt("status"));
        detail.setHoursWorked(rs.getInt("hours_worked"));
        detail.setTotalPrice(rs.getDouble("total_price"));
        return detail;
    }
    

   
}
