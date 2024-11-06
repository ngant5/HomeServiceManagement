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
        return "customer/contractDetail/edit"; // Trả về view chi tiết hợp đồng
    }
    
    
    
    // Cập nhật chi tiết hợp đồng
    @PostMapping("/update")
    public String updateContractDetail(@ModelAttribute ContractDetails contractDetail, Model model) {
        try {
            contractDetailService.updateContractDetail(contractDetail); // Cập nhật thông tin trong service
            model.addAttribute("message", "Cập nhật thành công!");
            return "redirect:/contractDetails/" + contractDetail.getContractId(); // Quay lại trang chi tiết hợp đồng
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi khi cập nhật thông tin.");
            return "customer/contractDetail/edit"; // Trở lại trang chỉnh sửa
        }
    }
}
