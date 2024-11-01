package net.codejava.service;

import net.codejava.model.ContractDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.codejava.repository.ContractDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractDetailService {

    @Autowired
    private ContractDetailsRepository contractDetailsRepository;
    private static final Logger logger = LoggerFactory.getLogger(ContractDetailService.class);


    // Tạo danh sách chi tiết hợp đồng
    public void createContractDetails(List<ContractDetails> contractDetails) {
        contractDetailsRepository.createContractDetails(contractDetails); // Phương thức này cần phải tồn tại trong repository
    }
    
    public ResponseEntity<ContractDetails> createContractDetail(ContractDetails contractDetail) {
        contractDetailsRepository.createContractDetail(contractDetail);
        return ResponseEntity.status(201).body(contractDetail); // Trả về ResponseEntity với status 201
    }


    // Lấy tất cả chi tiết hợp đồng
    public List<ContractDetails> getAllContractDetails() {
        return contractDetailsRepository.getAllContractDetails();
    }

    // Lấy chi tiết hợp đồng theo ID
    public ContractDetails getContractDetailById(int id) {
        return contractDetailsRepository.getContractDetailById(id);
    }

    // Cập nhật chi tiết hợp đồng
    public void updateContractDetail(ContractDetails contractDetail) {
        contractDetailsRepository.updateContractDetail(contractDetail);
    }

    // Xóa chi tiết hợp đồng
    public void deleteContractDetail(int id) {
        contractDetailsRepository.deleteContractDetail(id);
    }

    // Lấy chi tiết hợp đồng theo contractId
    public List<ContractDetails> getContractDetailsByContractId(int contractId) {
        List<ContractDetails> details = contractDetailsRepository.findByContractId(contractId);
        if (details.isEmpty()) {
            logger.warn("No contract details found for contract_id: {}", contractId);
        }
        return details;
    }

}
