package net.codejava.controller;

import net.codejava.model.ContractDetails;
import net.codejava.service.ContractDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/contractDetails")
public class ContractDetailController {

    @Autowired
    private ContractDetailService contractDetailService;

    // Lấy tất cả chi tiết hợp đồng
    @GetMapping
    public String getAllContractDetails(Model model) {
        List<ContractDetails> details = contractDetailService.getAllContractDetails();
        model.addAttribute("contractDetails", details);
        return "customer/contractDetail/list"; // Trả về view danh sách hợp đồng
    }

    // Lấy chi tiết hợp đồng theo ID
    @GetMapping("/{contractId}")
    public String getContractDetails(@PathVariable int contractId, Model model) {
        List<ContractDetails> details = contractDetailService.getContractDetailsByContractId(contractId);
        if (details == null || details.isEmpty()) {
            model.addAttribute("error", "No contract details found for contract_id: " + contractId);
            return "error"; // Trả về view lỗi
        }
        model.addAttribute("contractDetails", details);
        return "customer/contractDetail/view"; // Trả về view chi tiết hợp đồng
    }
    
    
}
