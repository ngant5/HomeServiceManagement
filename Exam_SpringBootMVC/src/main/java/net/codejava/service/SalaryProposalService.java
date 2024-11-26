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
}