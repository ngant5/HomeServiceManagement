package net.codejava.controller.Admin;

import net.codejava.service.SalaryProposalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/admin/salary-proposals")
public class AdminSalaryProposalController {
    private final SalaryProposalService salaryProposalService;

    public AdminSalaryProposalController(SalaryProposalService salaryProposalService) {
        this.salaryProposalService = salaryProposalService;
    }

    @GetMapping("")
    public String listProposals(Model model) {
        model.addAttribute("proposals", salaryProposalService.listAllProposals());
        return "admin/salary_proposal_list";
    }

    @PostMapping("/review")
    public String reviewProposal(@RequestParam int proposalId, 
                                 @RequestParam String action, 
                                 @RequestParam(required = false) String refusalReason, 
                                 Model model) {
        int approvalStatus = action.equals("approve") ? 1 : 2; // 1 for approved, 2 for rejected
        int result = salaryProposalService.reviewProposal(proposalId, approvalStatus, action.equals("reject") ? refusalReason : null);
        model.addAttribute("message", result > 0 ? "Proposal reviewed successfully" : "Failed to review proposal");
        return "redirect:/admin/salary-proposals";
    }
}