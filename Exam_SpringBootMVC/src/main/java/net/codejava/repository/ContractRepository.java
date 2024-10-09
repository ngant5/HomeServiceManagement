package net.codejava.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import net.codejava.model.Contracts;
import net.codejava.model.Customers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ContractRepository {

	@Autowired
    private JdbcTemplate jdbcTemplate;

    public ContractRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Contracts> findAll() {
        String sql = "SELECT * FROM Contracts";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Contracts.class));
    }

    private static class ContractRowMapper implements RowMapper<Contracts> {
        @Override
        public Contracts mapRow(ResultSet rs, int rowNum) throws SQLException {
            Contracts contract = new Contracts();
            contract.setContractId(rs.getInt("contract_id"));
            contract.setCustomerId(rs.getInt("customer_id"));
            contract.setContractStatus(rs.getInt("contract_status"));
            contract.setTotalPrice(rs.getInt("total_price"));
            contract.setPaymentStatus(rs.getInt("payment_status"));
            contract.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            contract.setContractFile(rs.getString("contract_file"));
            return contract;
        }
    }

    public Contracts findById(int contractId) {
        String sql = "SELECT * FROM Contracts WHERE contract_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{contractId}, new ContractRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }
    }


}
