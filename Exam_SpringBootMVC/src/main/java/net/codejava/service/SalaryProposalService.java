package net.codejava.service;

import net.codejava.model.SalaryProposals;
import net.codejava.repository.SalaryProposalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalaryProposalService {
    private final SalaryProposalRepository salaryProposalRepository;

    public SalaryProposalService(SalaryProposalRepository salaryProposalRepository) {
        this.salaryProposalRepository = salaryProposalRepository;
    }

    public int createProposal(SalaryProposals proposal) {
        proposal.setCreatedAt(LocalDateTime.now());
        return salaryProposalRepository.addProposal(proposal);
    }

    public List<SalaryProposals> listAllProposals() {
        return salaryProposalRepository.getAllProposals();
    }

    public int reviewProposal(int proposalId, int approvalStatus, String refusalReason) {
        return salaryProposalRepository.updateProposalStatus(proposalId, approvalStatus, refusalReason);
    }
    
    public boolean updateSalary(Long employeeId, double salary) {
        int result = salaryProposalRepository.updateSalary(employeeId, salary);
        return result > 0;  
    }

    public double getProposedSalary(Long employeeId) {
        List<SalaryProposals> proposals = salaryProposalRepository.getAllProposals();
        
        for (SalaryProposals proposal : proposals) {
            if (proposal.getEmployeeId().equals(employeeId) && proposal.getApprovalStatus() == 1) {
                return proposal.getProposedSalary();  
            }
        }
        
        return 0;  
    }

}