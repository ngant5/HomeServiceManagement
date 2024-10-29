package net.codejava.service;

import net.codejava.model.ContractDetails;
import net.codejava.repository.ContractDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractDetailService {

    @Autowired
    private ContractDetailsRepository contractDetailsRepository;

    public void createContractDetails(List<ContractDetails> contractDetails) {
        contractDetailsRepository.createContractDetail(contractDetails);
    }
    
    // Thêm phương thức tạo chi tiết hợp đồng đơn lẻ
    public void createContractDetail(ContractDetails contractDetail) {
        contractDetailsRepository.createContractDetail(contractDetail);
    }

    public List<ContractDetails> getAllContractDetails() {
        return contractDetailsRepository.getAllContractDetails();
    }

    public ContractDetails getContractDetailById(int id) {
        return contractDetailsRepository.getContractDetailById(id);
    }

    public void updateContractDetail(ContractDetails contractDetail) {
        contractDetailsRepository.updateContractDetail(contractDetail);
    }

    public void deleteContractDetail(int id) {
        contractDetailsRepository.deleteContractDetail(id);
    }

    public List<ContractDetails> getContractDetailsByContractId(int contractId) {
        return contractDetailsRepository.findByContractId(contractId);
    }
}
