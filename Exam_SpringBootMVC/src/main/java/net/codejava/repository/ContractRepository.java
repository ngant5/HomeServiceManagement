package net.codejava.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import net.codejava.model.ContractDetails;
import net.codejava.model.Contracts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class ContractRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger logger = Logger.getLogger(ContractRepository.class.getName());

    public List<Contracts> findAll() {
        String sql = "SELECT * FROM Contracts";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Contracts.class));
    }

    public Contracts findById(int contractId) {
        String sql = "SELECT * FROM Contracts WHERE contract_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{contractId}, new ContractRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.log(Level.WARNING, "No contract found with ID: " + contractId);
            return null; 
        }
    }

    public Contracts createContract(Contracts contract) {
        if (contract.getCreatedAt() == null) {
            contract.setCreatedAt(LocalDateTime.now());
        }
        logger.log(Level.INFO, "Creating contract: " + contract);
        Integer generatedId = insertContract(contract);
        if (generatedId == null) {
            throw new RuntimeException("Could not retrieve generated ID.");
        }
        contract.setContractId(generatedId);
        return contract;
    }

    private Integer insertContract(Contracts contract) {
        String sql = "INSERT INTO Contracts (customer_id, contract_status, total_price, payment_status, contract_file, created_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql, contract.getCustomerId(), contract.getContractStatus(), 
                contract.getTotalPrice(), contract.getPaymentStatus(), contract.getContractFile(), contract.getCreatedAt());
            
            // Lấy ID hợp đồng mới được tạo
            return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "Error inserting contract: " + e.getMessage(), e);
            throw new RuntimeException("Could not retrieve generated ID.", e);
        }
    }

    public void saveContractDetails(int contractId, List<ContractDetails> details) {
        if (details == null || details.isEmpty()) {
            logger.log(Level.WARNING, "Contract details list is empty.");
            return;
        }
        for (ContractDetails detail : details) {
            insertContractDetail(contractId, detail);
        }
    }

    private void insertContractDetail(int contractId, ContractDetails detail) {
        String sql = "INSERT INTO Contract_Details (contract_id, emp_service_id, service_address, service_phone, start_date, end_date, status, hours_worked, total_price) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, contractId, detail.getEmpServiceId(), detail.getServiceAddress(),
                    detail.getServicePhone(), detail.getStartDate(), detail.getEndDate(),
                    detail.getStatus(), detail.getHoursWorked(), detail.getTotalPrice());
        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "Error saving contract detail for contract ID " + contractId + ": " + e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving contract detail.", e);
        }
    }

    private static class ContractRowMapper implements RowMapper<Contracts> {
        @Override
        public Contracts mapRow(ResultSet rs, int rowNum) throws SQLException {
            Contracts contract = new Contracts();
            contract.setContractId(rs.getInt("contract_id"));
            contract.setCustomerId(rs.getInt("customer_id"));
            contract.setContractStatus(rs.getInt("contract_status"));
            contract.setTotalPrice(rs.getDouble("total_price"));
            contract.setPaymentStatus(rs.getInt("payment_status"));
            contract.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            contract.setContractFile(rs.getString("contract_file"));
            return contract;
        }
    }
}
