package net.codejava.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.codejava.model.ContractDetails;
import net.codejava.model.Contracts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class EmployeeContractRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeContractRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Contracts> getContractsByEmployeeIdWithPagination(int employeeId, int page, int size, Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT c.contract_id, c.customer_id, c.contract_status, c.total_price, c.payment_status, "
                   + "c.created_at, c.contract_file, cd.start_date, cd.end_date, c.employee_id "
                   + "FROM Contracts c "
                   + "JOIN Contract_Details cd ON c.contract_id = cd.contract_id "
                   + "WHERE c.employee_id = ? "
                   + "AND (? IS NULL OR cd.start_date >= ?) "
                   + "AND (? IS NULL OR cd.end_date <= ?) "
                   + "ORDER BY c.created_at DESC "
                   + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        return jdbcTemplate.query(sql, new Object[]{employeeId, startDate, startDate, endDate, endDate, (page - 1) * size, size}, this::mapRowToContract);
    }

    public int countContractsByEmployee(int employeeId, Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT COUNT(*) FROM Contracts c "
                   + "JOIN Contract_Details cd ON c.contract_id = cd.contract_id "
                   + "WHERE c.employee_id = ? "
                   + "AND (? IS NULL OR cd.start_date >= ?) "
                   + "AND (? IS NULL OR cd.end_date <= ?)";

        return jdbcTemplate.queryForObject(sql, new Object[]{employeeId, startDate, startDate, endDate, endDate}, Integer.class);
    }
    
	public void updateContractStatus(int contractId, int contractStatus) {
		String sql = "UPDATE Contracts SET contract_status = ? WHERE contract_id = ?";
		jdbcTemplate.update(sql, contractStatus, contractId);
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
        contract.setEmployeeId(rs.getInt("employee_id"));

        ContractDetails contractDetail = new ContractDetails();
        contractDetail.setStartDate(rs.getTimestamp("start_date").toLocalDateTime().toLocalDate());
        contractDetail.setEndDate(rs.getTimestamp("end_date").toLocalDateTime().toLocalDate());

        contract.setContractDetail(contractDetail);

        return contract;
    }
}


