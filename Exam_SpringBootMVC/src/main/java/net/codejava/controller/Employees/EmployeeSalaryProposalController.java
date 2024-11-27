package net.codejava.controller.Employees;

import net.codejava.model.SalaryProposals;
import net.codejava.service.SalaryProposalService;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/employees/salary-proposal")
public class EmployeeSalaryProposalController {
    private final SalaryProposalService salaryProposalService;

    public EmployeeSalaryProposalController(SalaryProposalService salaryProposalService) {
        this.salaryProposalService = salaryProposalService;
    }

    @PostMapping("/submit")
    public String submitProposal(@RequestParam int employeeId, 
                                 @RequestParam int proposedSalary, 
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate, 
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate, 
                                 Model model) {
        SalaryProposals proposal = new SalaryProposals();
        proposal.setEmployeeId(employeeId);
        proposal.setProposedSalary(proposedSalary);
        proposal.setFromDate(fromDate); // Now using LocalDate
        proposal.setToDate(toDate);     // Now using LocalDate
        proposal.setApprovalStatus(0); // Default status as pending (0)

        int result = salaryProposalService.createProposal(proposal);
        model.addAttribute("message", result > 0 ? "Proposal submitted successfully" : "Failed to submit proposal");
        return "employees/emp_update_info";
    }
}