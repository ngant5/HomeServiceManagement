package net.codejava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import jakarta.servlet.http.HttpSession;
import net.codejava.model.ContractDetails;
import net.codejava.model.Contracts;
import net.codejava.model.EmployeeServices;
import net.codejava.repository.EmployeeServicesRepository;
import net.codejava.repository.ServiceRepository;
import net.codejava.service.ContractDetailService;
import net.codejava.service.ContractService;
import net.codejava.service.EmployeeServicesService;
import net.codejava.service.ServiceService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    
    @Autowired
    private EmployeeServicesService employeeServicesService;


    @Autowired
    private ContractDetailService contractDetailService;
    
    @Autowired
    private EmployeeServicesRepository employeeServiceRepository;
    
    @Autowired
    private ServiceRepository serviceRepository; 
    
    @Autowired
    private ServiceService serviceService; 
    

        @GetMapping("/create")
        public String showCreateContractForm(Model model) {
            model.addAttribute("contract", new Contracts());
            return "contract/create"; // Tên trang tạo hợp đồng
        }

        @PostMapping("/create")
        public String createContract(@ModelAttribute Contracts contract, 
        							@RequestParam double servicePrice, 
        							HttpSession session) {
            Integer customerId = (Integer) session.getAttribute("customerId");

            // Bước 1: Kiểm tra customerId
            if (customerId == null) {
                System.err.println("Error: customerId is null. Redirecting to error page.");
                return "redirect:/error"; // Chuyển đến trang lỗi nếu không tìm thấy customerId
            }

            // Bước 2: Thiết lập customerId cho hợp đồng
            contract.setCustomerId(customerId);
            contract.setContractStatus(0); // Mặc định là 0
            contract.setTotalPrice(servicePrice); // Thiết lập giá từ servicePrice
            contract.setPaymentStatus(0); // Mặc định là 0
            contract.setCreatedAt(LocalDateTime.now()); // Thiết lập thời gian hiện tại
            contract.setContractFile(null);
            System.out.println("Creating contract for customerId: " + customerId);
            System.out.println("Contract details: Status: " + contract.getContractStatus() +
                               ", Total Price: " + contract.getTotalPrice() +
                               ", Payment Status: " + contract.getPaymentStatus() +
                               ", Created At: " + contract.getCreatedAt() +
                               ", Contract File: " + contract.getContractFile());


            try {
                // Bước 3: Tạo hợp đồng
                int contractId = contractService.createContract(contract, servicePrice);
                System.out.println("Contract created successfully with contractId: " + contractId);
                
                // Bước 4: Lưu contractId vào session
                session.setAttribute("contractId", contractId);
                return "redirect:/contracts/checkout"; // Chuyển đến trang checkout
            } catch (Exception e) {
                // Bước 5: Xử lý lỗi và log thông tin chi tiết
                System.err.println("Error creating contract: " + e.getMessage());
                e.printStackTrace(); // In stack trace để có thông tin chi tiết hơn
                return "redirect:/error"; // Chuyển đến trang lỗi
            }
        }


        @GetMapping("/checkout")
        public String showCheckoutForm(HttpSession session, Model model) {
            Integer contractId = (Integer) session.getAttribute("contractId");
            if (contractId != null) {
                model.addAttribute("contractId", contractId);
                // Thêm các thông tin cần thiết khác nếu cần
                return "customer/contract/create"; // Tên trang checkout
            }
            return "redirect:/error"; // Chuyển đến trang lỗi nếu không tìm thấy contractId
        }

        @PostMapping("/checkout")
        public String finalizeContract(@RequestParam String serviceAddress,
                                       @RequestParam String servicePhone,
                                       @RequestParam String startDate,
                                       @RequestParam String endDate,
                                       @RequestParam int hoursWorked,
                                       HttpSession session,
                                       Model model) {
            Integer contractId = (Integer) session.getAttribute("contractId");
            if (contractId != null) {
                // Tạo chi tiết hợp đồng
                ContractDetails contractDetail = new ContractDetails();
                contractDetail.setContractId(contractId);
                contractDetail.setServiceAddress(serviceAddress);
                contractDetail.setServicePhone(servicePhone);
                contractDetail.setStartDate(LocalDate.parse(startDate).atStartOfDay());
                contractDetail.setEndDate(LocalDate.parse(endDate).atStartOfDay());
                contractDetail.setHoursWorked(hoursWorked);
                contractDetail.setStatus(0); // Set trạng thái ban đầu

                // Gọi API để tạo chi tiết hợp đồng
                ResponseEntity<ContractDetails> response = contractDetailService.createContractDetail(contractDetail);
                if (response.getStatusCode().is2xxSuccessful()) {
                    return "contract/success"; // Chuyển đến trang thành công
                }
            }
            return "redirect:/error"; // Chuyển đến trang lỗi nếu không tìm thấy contractId
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
