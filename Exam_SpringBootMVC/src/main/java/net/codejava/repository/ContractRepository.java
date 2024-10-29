package net.codejava.repository;

import net.codejava.model.Contracts;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ContractRepository {

    private final JdbcTemplate jdbcTemplate;

    public ContractRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createContract(Contracts contract) {
        String sql = "INSERT INTO Contracts (customer_id, contract_status, total_price, payment_status, created_at, contract_file) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, contract.getCustomerId(), contract.getContractStatus(), 
                                    contract.getTotalPrice(), contract.getPaymentStatus(), 
                                    contract.getCreatedAt(), contract.getContractFile());
    }
    
    public List<Contracts> getAllContracts() {
        String sql = "SELECT * FROM Contracts";
        return jdbcTemplate.query(sql, this::mapRowToContract);
    }

    public Contracts getContractById(int contractId) {
        String sql = "SELECT * FROM Contracts WHERE contract_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToContract, contractId);
    }

    public void updateContract(Contracts contract) {
        String sql = "UPDATE Contracts SET customer_id = ?, contract_status = ?, total_price = ?, " +
                     "payment_status = ?, contract_file = ? WHERE contract_id = ?";
        jdbcTemplate.update(sql, contract.getCustomerId(), contract.getContractStatus(), 
                            contract.getTotalPrice(), contract.getPaymentStatus(), 
                            contract.getContractFile(), contract.getContractId());
    }

    public void deleteContract(int contractId) {
        String sql = "DELETE FROM Contracts WHERE contract_id = ?";
        jdbcTemplate.update(sql, contractId);
    }

    private Contracts mapRowToContract(ResultSet rs, int rowNum) throws SQLException {
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
