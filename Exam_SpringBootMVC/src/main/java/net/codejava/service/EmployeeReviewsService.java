package net.codejava.service;

import net.codejava.model.EmployeeReviews;
import net.codejava.model.Employees;
import net.codejava.model.ContractDetails;
import net.codejava.repository.EmployeeRepository;
import net.codejava.repository.EmployeeReviewsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeReviewsService {

    private final EmployeeReviewsRepository employeeReviewsRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeReviewsService(EmployeeReviewsRepository employeeReviewsRepository, EmployeeRepository employeeRepository) {
        this.employeeReviewsRepository = employeeReviewsRepository;
        this.employeeRepository = employeeRepository;
    }

    // Lấy tất cả đánh giá nhân viên theo contractId
    public List<EmployeeReviews> getReviewsByContractId(int contractId) {
        // Lấy danh sách ContractDetails theo contractId
        List<ContractDetails> contractDetails = employeeReviewsRepository.findContractDetailsByContractId(contractId);
        
        // Danh sách chứa các đánh giá nhân viên
        List<EmployeeReviews> employeeReviews = new ArrayList<>();

        // Lặp qua các chi tiết hợp đồng để lấy các đánh giá
        for (ContractDetails detail : contractDetails) {
            List<EmployeeReviews> reviews = employeeReviewsRepository.findReviewsByContractDetailId(detail.getContractDetailId());
            employeeReviews.addAll(reviews);  // Thêm các đánh giá vào danh sách
        }

        return employeeReviews;  // Trả về danh sách đánh giá nhân viên
    }

    // Lấy tất cả emp_service_id theo contractId
    public List<Integer> getEmpServiceIdsByContractId(int contractId) {
        return employeeReviewsRepository.findEmpServiceIdsByContractId(contractId);
    }
    
    public List<String> getEmployeeNamesByServiceId(int serviceId) {
        return employeeRepository.findEmployeeNamesByServiceId(serviceId);
    }


}
