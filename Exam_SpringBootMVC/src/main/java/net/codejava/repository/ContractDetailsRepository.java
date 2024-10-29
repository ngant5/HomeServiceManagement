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

    public void createContractDetail(List<ContractDetails> contractDetails) {
        String sql = "INSERT INTO Contract_Details (contract_id, emp_service_id, service_address, service_phone, start_date, end_date, status, hours_worked, total_price) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        for (ContractDetails detail : contractDetails) {
            jdbcTemplate.update(sql, detail.getContractId(), detail.getEmpServiceId(), 
                                detail.getServiceAddress(), detail.getServicePhone(), 
                                detail.getStartDate(), detail.getEndDate(), 
                                detail.getStatus(), detail.getHoursWorked(), 
                                detail.getTotalPrice());
        }
    }

    public void createContractDetail(ContractDetails contractDetail) {
        String sql = "INSERT INTO Contract_Details (contract_id, emp_service_id, service_address, service_phone, start_date, end_date, status, hours_worked, total_price) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, contractDetail.getContractId(), contractDetail.getEmpServiceId(), 
                            contractDetail.getServiceAddress(), contractDetail.getServicePhone(), 
                            contractDetail.getStartDate(), contractDetail.getEndDate(), 
                            contractDetail.getStatus(), contractDetail.getHoursWorked(), 
                            contractDetail.getTotalPrice());
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
        detail.setServiceAddress(rs.getString("service_address"));
        detail.setServicePhone(rs.getString("service_phone"));
        detail.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
        detail.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());
        detail.setStatus(rs.getInt("status"));
        detail.setHoursWorked(rs.getInt("hours_worked"));
        detail.setTotalPrice(rs.getDouble("total_price"));
        return detail;
    }

    public List<ContractDetails> getAllContractDetails() {
        String sql = "SELECT * FROM Contract_Details";
        return jdbcTemplate.query(sql, this::mapRowToContractDetail);
    }

    public ContractDetails getContractDetailById(int id) {
        String sql = "SELECT * FROM Contract_Details WHERE contract_detail_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToContractDetail, id);
    }

    public void updateContractDetail(ContractDetails contractDetail) {
        String sql = "UPDATE Contract_Details SET contract_id = ?, emp_service_id = ?, service_address = ?, service_phone = ?, start_date = ?, end_date = ?, status = ?, hours_worked = ?, total_price = ? WHERE contract_detail_id = ?";
        jdbcTemplate.update(sql, contractDetail.getContractId(), contractDetail.getEmpServiceId(),
                contractDetail.getServiceAddress(), contractDetail.getServicePhone(),
                contractDetail.getStartDate(), contractDetail.getEndDate(),
                contractDetail.getStatus(), contractDetail.getHoursWorked(),
                contractDetail.getTotalPrice(), contractDetail.getContractDetailId());
    }

    public void deleteContractDetail(int id) {
        String sql = "DELETE FROM Contract_Details WHERE contract_detail_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
