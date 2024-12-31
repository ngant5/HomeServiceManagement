package net.codejava.controller.Customer;

import net.codejava.model.EmployeeReviews;
import net.codejava.service.EmployeeReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class EmployeeReviewsController {

    private final EmployeeReviewsService employeeReviewsService;

    @Autowired
    public EmployeeReviewsController(EmployeeReviewsService employeeReviewsService) {
        this.employeeReviewsService = employeeReviewsService;
    }

    // Lấy tất cả đánh giá của một hợp đồng
    @GetMapping("/{contractId}")
    public String getReviewByContractId(@PathVariable int contractId, Model model) {
        List<EmployeeReviews> reviews = employeeReviewsService.getReviewByContractId(contractId); // Lấy các đánh giá
        model.addAttribute("reviews", reviews);  // Thêm danh sách đánh giá vào model
        model.addAttribute("contractId", contractId); // Thêm contractId vào model để hiển thị trên trang
        return "reviews";  // Trả về view reviews.html
    }
}
