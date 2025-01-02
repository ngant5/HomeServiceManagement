package net.codejava.controller.Customer;

import net.codejava.model.EmployeeReviews;
import net.codejava.model.Employees;
import net.codejava.service.EmployeeReviewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EmployeeReviewsController {

    private final EmployeeReviewsService employeeReviewsService;

    public EmployeeReviewsController(EmployeeReviewsService employeeReviewsService) {
        this.employeeReviewsService = employeeReviewsService;
    }

    @GetMapping("/employee-reviews/view/{contractId}")
    public String viewReviews(@PathVariable("contractId") int contractId, Model model) {
        // Lấy danh sách các đánh giá nhân viên theo contractId
        List<EmployeeReviews> employeeReviews = employeeReviewsService.getReviewsByContractId(contractId);

        // Lấy danh sách emp_service_id theo contractId
        List<Integer> empServiceIds = employeeReviewsService.getEmpServiceIdsByContractId(contractId);

        // Danh sách lưu trữ tên nhân viên
        List<String> employeeNames = new ArrayList<>();
        System.out.println("Emp Service IDs: " + empServiceIds);
        // Lặp qua các empServiceIds và lấy thông tin tên nhân viên
        for (Integer serviceId : empServiceIds) {
            // Lấy tên nhân viên theo serviceId
            List<String> names = employeeReviewsService.getEmployeeNamesByServiceId(serviceId);
            System.out.println("Names for Service ID " + serviceId + ": " + names);

            employeeNames.addAll(names);  // Thêm tên nhân viên vào danh sách
        }

        // Thêm vào model để hiển thị trong view
        model.addAttribute("contractId", contractId);
        model.addAttribute("empServiceIds", empServiceIds);
        model.addAttribute("employeeReviews", employeeReviews);
        model.addAttribute("employeeNames", employeeNames);  // Truyền danh sách tên nhân viên vào model

        return "customer/reviews/form";  // Trả về view HTML
    }
}
