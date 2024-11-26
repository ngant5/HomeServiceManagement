package net.codejava.repository;

import net.codejava.model.SalaryProposals;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class SalaryProposalRepository {
    private final JdbcTemplate jdbcTemplate;

    public SalaryProposalRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Insert a new salary proposal
    public int addProposal(SalaryProposals proposal) {
        String sql = "INSERT INTO Salary_Proposals (employee_id, proposed_salary, from_date, to_date, approval_status, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, 
            proposal.getEmployeeId(),
            proposal.getProposedSalary(),
            proposal.getFromDate(),
            proposal.getToDate(),
            proposal.getApprovalStatus(),
            proposal.getCreatedAt()
        );
    }

 // Get all salary proposals
    public List<SalaryProposals> getAllProposals() {
        String sql = "SELECT * FROM Salary_Proposals";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            SalaryProposals proposal = new SalaryProposals();
            proposal.setProposalId(rs.getInt("proposal_id"));
            proposal.setEmployeeId(rs.getInt("employee_id"));
            proposal.setProposedSalary(rs.getInt("proposed_salary"));
            
            // Retrieve from_date and to_date as LocalDate
            proposal.setFromDate(rs.getDate("from_date").toLocalDate());
            proposal.setToDate(rs.getDate("to_date").toLocalDate());
            
            proposal.setApprovalStatus(rs.getInt("approval_status"));
            
            // Retrieve created_at as LocalDateTime
            proposal.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            proposal.setRefusalReason(rs.getString("refusal_reason"));
            
            return proposal;
        });
    }
    	
    // Update proposal status and refusal reason if applicable
    public int updateProposalStatus(int proposalId, int approvalStatus, String refusalReason) {
        String sql = "UPDATE Salary_Proposals SET approval_status = ?, refusal_reason = ? WHERE proposal_id = ?";
        return jdbcTemplate.update(sql, approvalStatus, refusalReason, proposalId);
    }
}