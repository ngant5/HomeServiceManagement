package net.codejava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;



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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
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
        Integer customerId = (Integer) session.getAttribute("customerId");

        if (customerId == null) {
            logger.error("Error: customerId is not found in session.");
            return "redirect:/customer/login";
        }

        // Lấy danh sách hợp đồng theo customerId
        List<Contracts> contracts = contractService.getContractsByCustomerId(customerId);
        logger.info("Number of contracts found: {}", contracts.size());


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
        							@RequestParam(required = true) Double servicePrice, 
        							@RequestParam int empServiceId,
        							HttpSession session) {
            Integer customerId = (Integer) session.getAttribute("customerId");
            System.out.println("Received servicePrice from request: " + servicePrice);

            session.setAttribute("servicePrice", servicePrice);
            System.out.println("servicePrice: " + servicePrice);
            // Bước 1: Kiểm tra customerId
            if (customerId == null) {
                System.err.println("Error: customerId is null. Redirecting to error page.");
                return "redirect:/customer/login"; 
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
             
             // Trong controller trước khi lưu giá trị vào session
                System.out.println("Before saving to session, servicePrice: " + servicePrice);

                // Kiểm tra xem session đã có giá trị cũ chưa
                Double currentSessionServicePrice = (Double) session.getAttribute("servicePrice");
                System.out.println("Current session servicePrice: " + currentSessionServicePrice);

                // Lưu vào session và kiểm tra lại
                session.setAttribute("servicePrice", servicePrice);
                System.out.println("Service Price saved in session: " + session.getAttribute("servicePrice"));

                // Sau đó lưu giá trị mới vào session
                session.setAttribute("servicePrice", servicePrice);

                // Kiểm tra lại giá trị đã lưu trong session
                System.out.println("Service Price saved in session: " + servicePrice);

                System.out.println(" empServiceId saved in session: " + empServiceId);

                
                EmployeeServices employeeServices = employeeServicesService.findByEmpServiceId(empServiceId);
                if (employeeServices != null && employeeServices.getEmployee() != null) {
                    Integer employeeId = employeeServices.getEmployee().getEmployeeId();  // Lấy employeeId
                    session.setAttribute("employee_id", employeeId);  
                    System.out.println("Employee ID saved in session: " + employeeId); 
                } else {
                    // Xử lý nếu không tìm thấy EmployeeServices hợp lệ
                    System.err.println("Error: Employee not found for empServiceId: " + empServiceId);
                    return "redirect:/error"; // Chuyển đến trang lỗi
                }
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
        	
        	Integer customerId = (Integer) session.getAttribute("customerId");
            if (customerId == null) {
                logger.error("Error: customerId is not found in session.");
                return "redirect:/customer/login"; 
            }

            
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
                model.addAttribute("employeeId", session.getAttribute("employee_id"));

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
                                       @RequestParam String hoursWorked,
                                       @RequestParam String contractType,
                                       @RequestParam String totalPrice,
                                       @RequestParam Integer employeeId,
                                       HttpSession session,
                                       Model model) {
        	Integer customerId = (Integer) session.getAttribute("customerId");
            if (customerId == null) {
                logger.error("Error: customerId is not found in session.");
                return "redirect:/customer/login";  
            }
            
            Integer contractId = (Integer) session.getAttribute("contractId");
            Integer empServiceId = (Integer) session.getAttribute("empServiceId"); 
            
            Double servicePrice = (Double) session.getAttribute("servicePrice");
            if (servicePrice == null) {
                logger.error("Error: servicePrice is not found in session.");
                return "redirect:/error"; // Nếu không có servicePrice trong session
            } else {
                logger.info("Service Price retrieved from session: {}", servicePrice);
            }

            logger.info("Finalizing contract for contractId: {} with empServiceId: {}", contractId, empServiceId);
            
           

            if (contractId != null) {
                // Tạo chi tiết hợp đồng
                ContractDetails contractDetail = new ContractDetails();
                contractDetail.setContractId(contractId);
                contractDetail.setEmpServiceId(empServiceId);
                contractDetail.setServiceAddress(serviceAddress);
                contractDetail.setServicePhone(servicePhone);
                contractDetail.setHoursWorked(hoursWorked);
                contractDetail.setStatus(0);
                contractDetail.setContractType(contractType);
                contractDetail.setTotalPrice(totalPrice);
                contractDetail.setEmployeeId(employeeId);
                totalPrice = totalPrice.replace("$", "").trim();
                if (!startDate.isEmpty()) {
                    try {
                        contractDetail.setStartDate(LocalDate.parse(startDate)); 
                    } catch (DateTimeParseException e) {
                        logger.error("Invalid start date format: {}", startDate);
                        return "redirect:/error";  // Nếu startDate không hợp lệ
                    }
                }

                if (!endDate.isEmpty()) {
                    try {
                        contractDetail.setEndDate(LocalDate.parse(endDate)); 
                    } catch (DateTimeParseException e) {
                        logger.error("Invalid end date format: {}", endDate);
                        return "redirect:/error";  // Nếu endDate không hợp lệ
                    }
                }


                // Gọi API để tạo chi tiết hợp đồng
                ResponseEntity<ContractDetails> response = contractDetailService.createContractDetail(contractDetail);
                if (response.getStatusCode().is2xxSuccessful()) {
                	logger.info("Contract detail created successfully.");
                	
                	    try {
                	        // Chuyển totalPrice từ String sang Double
                	        double totalPriceDouble = Double.parseDouble(totalPrice);
                	        contractService.updateContractTotalPrice(contractId, totalPriceDouble);
                	        logger.info("Contract updated with new totalPrice: {}", totalPriceDouble);
                	    } catch (NumberFormatException e) {
                	        logger.error("Invalid totalPrice format: {}", totalPrice); // Nếu không thể chuyển đổi thành double
                	        return "redirect:/error"; // Quay lại trang lỗi nếu totalPrice không hợp lệ
                	    }
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
    
    private static final String FILE_DIRECTORY = "uploads/contracts/";

    // Xử lý khi người dùng yêu cầu xem file PDF của hợp đồng
    @GetMapping("/view-file/{contractId}")
    public ResponseEntity<Resource> viewFile(@PathVariable Long contractId) throws Exception {
        // Giả sử bạn lưu tên file dưới dạng contractId.pdf
        String fileName = contractId + ".pdf"; 

        // Tạo đường dẫn đến file PDF
        Path filePath = Paths.get(FILE_DIRECTORY + fileName);
        
        // Kiểm tra nếu file tồn tại
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        // Trả về file PDF dưới dạng tài nguyên
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")  // Đảm bảo kiểu là PDF
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")  // Mở trực tiếp trong trình duyệt
                .body(resource);
    }
    
    @GetMapping("/{contractId}/payment")
    public String showPaymentPage(@PathVariable("contractId") int contractId, Model model) {
        // Lấy thông tin hợp đồng từ cơ sở dữ liệu
        Contracts contract = contractService.getContractById(contractId);
        model.addAttribute("contract", contract);  // Truyền thông tin hợp đồng vào model

        // Trả về trang payment
        return "customer/payment/create";
    }
}

