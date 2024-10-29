package net.codejava.controller;

import net.codejava.model.Contracts;
import net.codejava.model.ContractDetails;
import net.codejava.service.ContractService;
import net.codejava.service.ContractDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/contracts")
public class CustomerContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractDetailService contractDetailService;

    @GetMapping("/create")
    public String showCreateContractForm(Model model) {
        model.addAttribute("contract", new Contracts());
        return "contract/create";
    }

    @PostMapping("/create")
    public String createContract(@ModelAttribute Contracts contract, HttpSession session) {
        int customerId = (int) session.getAttribute("customerId"); // Lấy customerId từ session
        contract.setCustomerId(customerId);
        contract.setContractStatus(0); // Trạng thái mặc định
        contract.setPaymentStatus(0); // Trạng thái mặc định

        // Gọi service để lưu hợp đồng
        contractService.createContract(contract);

        // Sau khi tạo hợp đồng, có thể chuyển hướng đến trang khác hoặc hiển thị thông báo
        return "redirect:/contracts/success"; // Ví dụ chuyển hướng đến trang thành công
    }


    @GetMapping("/{id}")
    public String getContract(@PathVariable int id, Model model) {
        Contracts contract = contractService.getContractById(id);
        List<ContractDetails> details = contractDetailService.getContractDetailsByContractId(id);
        model.addAttribute("contract", contract);
        model.addAttribute("details", details);
        return "contract/details";
    }

    @GetMapping("/details/create")
    public String showCreateContractDetailsForm(@RequestParam int contractId, Model model) {
        model.addAttribute("contractId", contractId);
        model.addAttribute("contractDetails", new ContractDetails());
        return "contract/details/create";
    }

    @PostMapping("/details/create")
    public String createContractDetails(@RequestParam int contractId, @ModelAttribute List<ContractDetails> contractDetails) {
        contractDetails.forEach(detail -> detail.setContractId(contractId));
        contractDetailService.createContractDetails(contractDetails);
        return "redirect:/contracts/" + contractId;
    }

    @PostMapping("/{id}/delete")
    public String deleteContract(@PathVariable int id) {
        contractService.deleteContract(id);
        return "redirect:/contracts";
    }
}
