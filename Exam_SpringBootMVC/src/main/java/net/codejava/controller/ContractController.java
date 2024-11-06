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
    
    private static final Logger logger = LoggerFactory.getLogger(ContractController.class);

    @GetMapping
    public String showContractList(HttpSession session, Model model) {
        // Lấy customerId từ session
        Integer customerId = (Integer) session.getAttribute("customerId");

        // Kiểm tra nếu customerId không tồn tại trong session
        if (customerId == null) {
            logger.error("Error: customerId is not found in session.");
            return "redirect:/error";  // Chuyển đến trang lỗi nếu không tìm thấy customerId
        }

        // Lấy danh sách hợp đồng theo customerId
        List<Contracts> contracts = contractService.getContractsByCustomerId(customerId);

        // Thêm danh sách hợp đồng vào model
        model.addAttribute("contracts", contracts);
        return "customer/contract/list"; // Trả về view để hiển thị danh sách hợp đồng
    }

        @GetMapping("/create")
        public String showCreateContractForm(Model model) {
            model.addAttribute("contract", new Contracts());
            return "contract/create"; // Tên trang tạo hợp đồng
        }

        @PostMapping("/create")
        public String createContract(@ModelAttribute Contracts contract, 
        							@RequestParam(required = false) Double servicePrice, 
        							@RequestParam int empServiceId,
        							HttpSession session) {
            Integer customerId = (Integer) session.getAttribute("customerId");

            // Bước 1: Kiểm tra customerId
            if (customerId == null) {
                System.err.println("Error: customerId is null. Redirecting to error page.");
                return "redirect:/error"; // Chuyển đến trang lỗi nếu không tìm thấy customerId
            }
            
            if (empServiceId <= 0) {
                System.err.println("Error: empServiceId is invalid: " + empServiceId);
                return "redirect:/error"; // Chuyển đến trang lỗi
            }

            if (servicePrice == null) {
                servicePrice = (Double) session.getAttribute("servicePrice");  // Lấy từ session nếu không có trong request
            }
            
            if (servicePrice == null) {
                // Nếu không có servicePrice trong session, trả về lỗi
                System.err.println("Error: servicePrice not found in session or request.");
                return "redirect:/error";
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
            System.out.println("empServiceId: " + empServiceId);

            try {
                // Bước 3: Tạo hợp đồng
                int contractId = contractService.createContract(contract, servicePrice);
                System.out.println("Contract created successfully with contractId: " + contractId);
                
                // Bước 4: Lưu contractId vào session
                session.setAttribute("contractId", contractId);
                session.setAttribute("empServiceId", empServiceId);
                session.setAttribute("servicePrice", servicePrice);
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
            logger.info("Checking out with contractId: {}", contractId);

            if (contractId != null) {
                model.addAttribute("contractId", contractId);
                logger.info("ContractId found: {}", contractId);
                Integer empServiceId = (Integer) session.getAttribute("empServiceId");
                logger.info("empServiceId found: {}", empServiceId);


                if (empServiceId == null || empServiceId == 0) {
                    logger.warn("Invalid empServiceId: {}. Redirecting to error page.", empServiceId);
                    return "redirect:/error";
                }

                Double servicePrice = (Double) session.getAttribute("servicePrice");
                if (servicePrice == null) {
                    logger.warn("No totalPrice found in session. Cannot finalize contract.");
                    return "redirect:/error";
                }
                model.addAttribute("servicePrice", servicePrice);


                
                return "customer/contract/create"; // Tên trang checkout
            }
            logger.warn("No contractId found in session. Redirecting to error page.");
            return "redirect:/error"; 
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
            Integer empServiceId = (Integer) session.getAttribute("empServiceId"); 
            Double servicePrice = (Double) session.getAttribute("servicePrice"); 

            logger.info("Finalizing contract for contractId: {} with empServiceId: {}", contractId, empServiceId);

            if (servicePrice == null) {
                logger.warn("No totalPrice found in session. Cannot finalize contract.");
                return "redirect:/error"; // Chuyển đến trang lỗi
            }

            if (contractId != null) {
                // Tạo chi tiết hợp đồng
                ContractDetails contractDetail = new ContractDetails();
                contractDetail.setContractId(contractId);
                contractDetail.setEmpServiceId(empServiceId);
                contractDetail.setServiceAddress(serviceAddress);
                contractDetail.setServicePhone(servicePhone);
                contractDetail.setStartDate(LocalDate.parse(startDate).atStartOfDay());
                contractDetail.setEndDate(LocalDate.parse(endDate).atStartOfDay());
                contractDetail.setHoursWorked(hoursWorked);
                contractDetail.setStatus(0);
                contractDetail.setTotalPrice(servicePrice);

                // Gọi API để tạo chi tiết hợp đồng
                ResponseEntity<ContractDetails> response = contractDetailService.createContractDetail(contractDetail);
                if (response.getStatusCode().is2xxSuccessful()) {
                	

                	logger.info("Contract detail created successfully.");

                    return "customer/contract/success"; // Chuyển đến trang thành công
                }else {
                    logger.error("Failed to create contract detail. Response status: {}", response.getStatusCode());
                }
            } else {
                logger.warn("No contractId found. Cannot finalize contract.");
            }
            
            return "redirect:/error"; // Chuyển đến trang lỗi nếu không tìm thấy contractId
        }

       


    @GetMapping("/{id}")
    public String getContract(@PathVariable int id, Model model) {
        Contracts contract = contractService.getContractById(id);
        List<ContractDetails> details = contractDetailService.getContractDetailsByContractId(id); 

        model.addAttribute("contract", contract);
        model.addAttribute("details", details);
        logger.info("Number of contract details found: {}", details.size());

        return "customer/contract/detail";
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