package net.codejava.controller;

import net.codejava.model.ContractDetails;
import net.codejava.model.EmployeeContractSchedule;
import net.codejava.model.EmployeeServices;
import net.codejava.model.Employees;
import net.codejava.repository.EmployeeServicesRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.codejava.service.ContractDetailService;
import net.codejava.service.EmployeeContractScheduleService;
import net.codejava.service.EmployeeServicesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/contractDetails")
public class ContractDetailController {

	@Autowired
    private EmployeeServicesService employeeServicesService;
	
	@Autowired
    private EmployeeServicesRepository employeeServicesRepository;
	
    @Autowired
    private ContractDetailService contractDetailService;
    
    @Autowired
    private EmployeeContractScheduleService employeeContractScheduleService;

    private static final Logger logger = LoggerFactory.getLogger(ContractDetailController.class);

    // Lấy tất cả chi tiết hợp đồng
    @GetMapping
    public String getAllContractDetails(Model model) {
        List<ContractDetails> details = contractDetailService.getAllContractDetails();
        model.addAttribute("contractDetails", details);
        return "customer/contractDetail/list"; // Trả về view danh sách hợp đồng
    }
    
    @GetMapping("/schedule")
    public String getEmployeeSchedule(HttpSession session, Model model) {
        Integer employeeId = (Integer) session.getAttribute("employee_id");

        if (employeeId == null) {
            logger.error("Employee ID not found in session.");
            return "redirect:/login";  // Chuyển hướng đến trang login nếu không tìm thấy employee_id trong session
        }

        // Lấy lịch trình của nhân viên dựa trên employeeId từ EmployeeContractScheduleService
        List<EmployeeContractSchedule> schedules = employeeContractScheduleService.getSchedulesByEmployeeId(employeeId);

        if (schedules == null || schedules.isEmpty()) {
            logger.error("No schedule found for employee with ID: {}", employeeId);
            model.addAttribute("error", "No schedule available for this employee.");
            return "error";  // Nếu không có lịch trình, trả về trang lỗi
        }

        // Thêm lịch trình vào model để hiển thị trên trang
        model.addAttribute("schedules", schedules);

        return "customer/contractDetail/schedule";  // Trả về view hiển thị lịch trình
    }
    
    

    
    @GetMapping("/{contractId}")
    public String getContractDetails(@PathVariable int contractId, Model model, HttpSession session) {
    	// Lấy customerId từ session
        Integer customerId = (Integer) session.getAttribute("customerId");
        Integer employeeId = (Integer) session.getAttribute("employee_id");

        // Kiểm tra nếu customerId không tồn tại trong session
        if (customerId == null) {
            logger.error("Error: customerId is not found in session.");
            return "redirect:/customer/login";
        }
        // Lấy danh sách các chi tiết hợp đồng theo contractId
        List<ContractDetails> contractDetailsList = contractDetailService.getContractDetailsByContractId(contractId);
        
        // Log kích thước danh sách chi tiết hợp đồng
        logger.debug("Danh sách chi tiết hợp đồng có kích thước: {}", contractDetailsList.size());

        if (contractDetailsList.isEmpty()) {
            logger.error("Không tìm thấy chi tiết hợp đồng cho contract_id: {}", contractId);
            model.addAttribute("error", "Không tìm thấy chi tiết hợp đồng cho contract_id: " + contractId);
            return "error"; 
        }

        // Lấy ContractDetails từ danh sách (giả sử hợp đồng có thể có nhiều chi tiết, bạn lấy chi tiết đầu tiên)
        ContractDetails contractDetail = contractDetailsList.get(0); 
        logger.debug("Lấy chi tiết hợp đồng: {}", contractDetail);
        
        int contractDetailId = contractDetail.getContractDetailId();  // Đảm bảo rằng bạn lấy ID của chi tiết hợp đồng

        // Lấy empServiceId từ contractDetail
        int empServiceId = contractDetail.getEmpServiceId(); 
        logger.debug("EmpServiceId được lấy từ contractDetail: {}", empServiceId);

        // Tìm thông tin EmployeeServices thông qua empServiceId
        EmployeeServices employeeServices = employeeServicesService.findByEmpServiceId(empServiceId); 
        logger.debug("Tìm thấy EmployeeServices với empServiceId: {}", empServiceId);

        // Log thông tin chi tiết hợp đồng và nhân viên
        if (employeeServices != null) {
            logger.debug("Employee Full Name: {}", employeeServices.getEmployee() != null ? employeeServices.getEmployee().getFullname() : "Không có thông tin nhân viên");
            logger.debug("Service Name: {}", employeeServices.getService() != null ? employeeServices.getService().getServiceName() : "Không có thông tin dịch vụ");
        }

        // Kiểm tra nếu employeeServices hoặc employee là null
        if (employeeServices == null || employeeServices.getEmployee() == null) {
            logger.error("Không tìm thấy thông tin dịch vụ nhân viên với empServiceId: {}", empServiceId);
            model.addAttribute("error", "Không tìm thấy thông tin dịch vụ nhân viên với empServiceId: " + empServiceId);
            return "error"; 
        }

        if (employeeServices.getEmployee() != null) {
            logger.debug("Employee Full Name: {}", employeeServices.getEmployee().getFullname());
        } else {
            logger.error("Không có thông tin nhân viên cho empServiceId: {}", empServiceId);
        }

        if (employeeServices.getService() != null) {
            logger.debug("Service Name: {}", employeeServices.getService().getServiceName());
        } else {
            logger.error("Không có thông tin dịch vụ cho empServiceId: {}", empServiceId);
        }

        if (employeeServices.getEmployee() == null) {
            logger.error("Không có thông tin nhân viên cho empServiceId: {}", empServiceId);
            model.addAttribute("error", "Không có thông tin nhân viên cho empServiceId: " + empServiceId);
            return "error"; // Trả về trang lỗi nếu không có thông tin nhân viên
        }
        String employeeFullName = employeeServices.getEmployee().getFullname();
        String serviceName = employeeServices.getService() != null ? employeeServices.getService().getServiceName() : "Dịch vụ không có tên";

        // Thêm thông tin vào model để hiển thị trong view
        model.addAttribute("contractDetailId", contractDetailId);
        model.addAttribute("contractDetails", contractDetail); 
        model.addAttribute("employeeFullName", employeeFullName); 
        model.addAttribute("serviceName", serviceName); 
        model.addAttribute("employeeServices", employeeServices);
        model.addAttribute("employeeId", employeeId); 

        // Trả về view chi tiết hợp đồng
        logger.debug("Trả về view chi tiết hợp đồng cho contractId: {}", contractDetailId);
        return "customer/contractDetail/view"; // Tên view để hiển thị chi tiết hợp đồng
    }

    @GetMapping("/employees/{contractDetailId}")
    @ResponseBody
    public List<EmployeeServices> findByContractDetailId(@PathVariable int contractDetailId) {
    	List<EmployeeServices> employeeServicesList = employeeServicesRepository.findByContractDetailId(contractDetailId);
    	if (employeeServicesList.isEmpty()) {
            logger.warn("No employees found for contractDetailId: {}", contractDetailId);
        }
        return employeeServicesList;
    }



    @PostMapping("/update/{contractDetailId}")
    @ResponseBody
    public ContractDetails updateContractDetail(@PathVariable int contractDetailId, 
                                                @RequestBody ContractDetails contractDetail) {
        // Gán contractDetailId từ URL vào đối tượng contractDetail nhận từ request body
        contractDetail.setContractDetailId(contractDetailId);

        // Gọi service để cập nhật hoặc tạo mới
        return contractDetailService.updateContractDetail(contractDetail);  // Trả về bản ghi hợp đồng đã được cập nhật hoặc tạo mới
    }
}
