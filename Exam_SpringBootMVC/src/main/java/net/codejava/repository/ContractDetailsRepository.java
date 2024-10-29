package net.codejava.repository;

import net.codejava.model.ContractDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ContractDetailsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class ContractDetailsRowMapper implements RowMapper<ContractDetails> {
        @Override
        public ContractDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            ContractDetails details = new ContractDetails();
            details.setContractDetailId(rs.getInt("contract_detail_id"));
            details.setContractId(rs.getInt("contract_id"));
            details.setEmpServiceId(rs.getInt("emp_service_id"));
            details.setServiceAddress(rs.getString("service_address"));
            details.setServicePhone(rs.getString("service_phone"));
            details.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
            details.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());
            details.setStatus(rs.getInt("status"));
            details.setHoursWorked(rs.getInt("hours_worked"));
            details.setTotalPrice(rs.getDouble("total_price"));
            return details;
        }
    }

    public List<ContractDetails> findAll() {
        String sql = "SELECT * FROM contract_details";
        return jdbcTemplate.query(sql, new ContractDetailsRowMapper());
    }

    public Optional<ContractDetails> findById(int detailId) {
        String sql = "SELECT * FROM contract_details WHERE contract_detail_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, new ContractDetailsRowMapper(), detailId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void save(ContractDetails contractDetails) {
        String sql = "INSERT INTO contract_details (contract_id, emp_service_id, service_address, " +
                     "service_phone, start_date, end_date, status, hours_worked, total_price) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, contractDetails.getContractId(), 
                                    contractDetails.getEmpServiceId(), 
                                    contractDetails.getServiceAddress(), 
                                    contractDetails.getServicePhone(), 
                                    contractDetails.getStartDate(), 
                                    contractDetails.getEndDate(), 
                                    contractDetails.getStatus(), 
                                    contractDetails.getHoursWorked(), 
                                    contractDetails.getTotalPrice());
    }

    public void update(ContractDetails contractDetails) {
        String sql = "UPDATE contract_details SET emp_service_id = ?, service_address = ?, " +
                     "service_phone = ?, start_date = ?, end_date = ?, status = ?, " +
                     "hours_worked = ?, total_price = ? WHERE contract_detail_id = ?";
        jdbcTemplate.update(sql, contractDetails.getEmpServiceId(),
                                    contractDetails.getServiceAddress(),
                                    contractDetails.getServicePhone(),
                                    contractDetails.getStartDate(),
                                    contractDetails.getEndDate(),
                                    contractDetails.getStatus(),
                                    contractDetails.getHoursWorked(),
                                    contractDetails.getTotalPrice(),
                                    contractDetails.getContractDetailId());
    }

    public void deleteById(int detailId) {
        String sql = "DELETE FROM contract_details WHERE contract_detail_id = ?";
        jdbcTemplate.update(sql, detailId);
    }
}
