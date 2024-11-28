package net.codejava.repository;

import net.codejava.model.ContractDetails;
import net.codejava.model.Contracts;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ContractRepository {

	private final JdbcTemplate jdbcTemplate;

	public ContractRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Contracts> getContractsWithPagination(int page, int size, Timestamp startDate, Timestamp endDate) {
	    // SQL query để lấy hợp đồng theo phân trang và tìm kiếm theo ngày
	    String sql = "SELECT c.contract_id, c.customer_id, c.contract_status, c.total_price, c.payment_status, "
	               + "c.created_at, c.contract_file, cd.start_date, cd.end_date "
	               + "FROM Contracts c "
	               + "JOIN Contract_Details cd ON c.contract_id = cd.contract_id "
	               + "WHERE (? IS NULL OR cd.start_date >= ?) "
	               + "AND (? IS NULL OR cd.end_date <= ?) "
	               + "ORDER BY c.created_at DESC "
	               + "OFFSET ? ROWS "
	               + "FETCH NEXT ? ROWS ONLY";

	    // Truy vấn các hợp đồng thỏa mãn điều kiện
	    return jdbcTemplate.query(sql, new Object[] { startDate, startDate, endDate, endDate, (page - 1) * size, size }, this::mapRowToContract);
	}


	public int countContracts(Timestamp startDate, Timestamp endDate) {
	    // SQL để đếm tổng số hợp đồng thỏa mãn điều kiện
	    String sql = "SELECT COUNT(*) FROM Contracts c "
	               + "JOIN Contract_Details cd ON c.contract_id = cd.contract_id "
	               + "WHERE (? IS NULL OR cd.start_date >= ?) "
	               + "AND (? IS NULL OR cd.end_date <= ?)";

	    // Trả về số lượng hợp đồng thỏa mãn điều kiện
	    return jdbcTemplate.queryForObject(sql, new Object[] { startDate, startDate, endDate, endDate }, Integer.class);
	}



	public List<Contracts> findContractsByCustomerId(int customerId) {
		String sql = "SELECT * FROM Contracts WHERE customer_id = ? ORDER BY created_at DESC";
		return jdbcTemplate.query(sql, new Object[] { customerId }, this::mapRowToCustomerContract);
	}

	public int createContract(Contracts contract, double servicePrice) {
		System.out.println("Creating contract with customerId: " + contract.getCustomerId());

		System.out.println("Contract details: " + " Status: " + contract.getContractStatus() + ", Total Price: "
				+ contract.getTotalPrice() + ", Payment Status: " + contract.getPaymentStatus() + ", Created At: "
				+ contract.getCreatedAt() + ", Contract File: " + contract.getContractFile());

		if (contract.getCustomerId() == 0 || contract.getTotalPrice() <= 0 || contract.getCreatedAt() == null) {
			throw new IllegalArgumentException("Invalid contract details provided.");
		}

		String sql = "INSERT INTO Contracts (customer_id, contract_status, total_price, payment_status, created_at, contract_file) "
				+ "OUTPUT INSERTED.contract_id VALUES (?, ?, ?, ?, ?, ?)";

		try {
			// Thực hiện câu lệnh chèn và lấy ID cùng một lúc
			Integer generatedId = jdbcTemplate.queryForObject(sql,
					new Object[] { contract.getCustomerId(), contract.getContractStatus(), contract.getTotalPrice(),
							contract.getPaymentStatus(), Timestamp.valueOf(contract.getCreatedAt()),
							contract.getContractFile() },
					Integer.class);

			if (generatedId == null) {
				System.err.println("Failed to retrieve generated ID for the contract.");
				throw new IllegalStateException("Failed to retrieve generated ID for the contract.");
			}

			contract.setContractId(generatedId);
			return generatedId;
		} catch (Exception e) {
			System.err.println("Database error during contract creation: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/*
	 * public List<Contracts> getAllContracts() { String sql =
	 * "SELECT * FROM Contracts"; String sql =
	 * "SELECT * FROM Contracts ORDER BY created_at DESC"; return
	 * jdbcTemplate.query(sql, this::mapRowToContract); }
	 */
	public List<Contracts> getAllContracts() {
		String sql = "SELECT c.contract_id, c.customer_id, c.contract_status, c.total_price, c.payment_status, "
				+ "c.created_at, c.contract_file, cd.start_date, cd.end_date " + "FROM Contracts c "
				+ "JOIN Contract_Details cd ON c.contract_id = cd.contract_id " + "ORDER BY c.created_at DESC";

		return jdbcTemplate.query(sql, this::mapRowToContract);
	}

	/*
	 * public Contracts getContractById(int contractId) { String sql =
	 * "SELECT * FROM Contracts WHERE contract_id = ?"; return
	 * jdbcTemplate.queryForObject(sql, this::mapRowToContract, contractId); }
	 */
	public Contracts getContractById(int contractId) {
		String sql = "SELECT c.contract_id, c.customer_id, c.contract_status, c.total_price, c.payment_status, "
                + "c.created_at, c.contract_file, cd.start_date, cd.end_date "
                + "FROM Contracts c "
                + "JOIN Contract_Details cd ON c.contract_id = cd.contract_id "
                + "WHERE c.contract_id = ?";
	    return jdbcTemplate.queryForObject(sql, this::mapRowToContract, contractId);
	}




	public void updateContractFile(Contracts contract) {
		String sql = "UPDATE contracts SET contract_file = ? WHERE contract_id = ?";
		jdbcTemplate.update(sql, contract.getContractFile(), contract.getContractId());
	}

	public void updateContract(Contracts contract) {
		String sql = "UPDATE Contracts SET customer_id = ?, contract_status = ?, total_price = ?, "
				+ "payment_status = ?, contract_file = ? WHERE contract_id = ?";
		jdbcTemplate.update(sql, contract.getCustomerId(), contract.getContractStatus(), contract.getTotalPrice(),
				contract.getPaymentStatus(), contract.getContractFile(), contract.getContractId());
	}

	public void updateContractStatus(int contractId, int contractStatus) {
		String sql = "UPDATE Contracts SET contract_status = ? WHERE contract_id = ?";
		jdbcTemplate.update(sql, contractStatus, contractId);
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

		ContractDetails contractDetail = new ContractDetails();
	    contractDetail.setStartDate(rs.getTimestamp("start_date").toLocalDateTime().toLocalDate());
	    contractDetail.setEndDate(rs.getTimestamp("end_date").toLocalDateTime().toLocalDate());
	    
	    
	    contract.setContractDetail(contractDetail);

		return contract;
	}
	


	private Contracts mapRowToCustomerContract(ResultSet rs, int rowNum) throws SQLException {

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