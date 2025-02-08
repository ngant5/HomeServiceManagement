package net.codejava.controller.Admin;

import net.codejava.service.SalaryProposalService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/admin/salary-proposals")
public class AdminSalaryProposalController {
    private final SalaryProposalService salaryProposalService;

    public AdminSalaryProposalController(SalaryProposalService salaryProposalService) {
        this.salaryProposalService = salaryProposalService;
    }
    private double getProposedSalary(Long employeeId) {
        return salaryProposalService.getProposedSalary(employeeId);
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
    
    @PutMapping("/updateSalary/{employeeId}")
    public ResponseEntity<Map<String, String>> updateSalary(
            @PathVariable Long employeeId) {
        
        Map<String, String> response = new HashMap<>();
        
        try {
            // Lấy proposed_salary cho nhân viên
            double proposedSalary = getProposedSalary(employeeId);

            // Kiểm tra nếu proposedSalary hợp lệ
            if (proposedSalary == 0) {
                response.put("status", "error");
                response.put("message", "Proposed salary not found for employee");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Gọi service để cập nhật lương cho nhân viên
            boolean result = salaryProposalService.updateSalary(employeeId, proposedSalary);
            
            if (result) {
                response.put("status", "success");
                response.put("message", "Salary updated successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Failed to update salary");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error occurred while updating salary");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
