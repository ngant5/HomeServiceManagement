package net.codejava.service;

import net.codejava.model.ContractDetails;
import net.codejava.repository.ContractDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ContractDetailService {

    @Autowired
    private ContractDetailsRepository contractDetailsRepository;

    private static final Logger logger = Logger.getLogger(ContractDetailService.class.getName());

    public List<ContractDetails> getAllContractDetails() {
        return contractDetailsRepository.findAll();
    }

    public ContractDetails getContractDetailById(int detailId) {
        return contractDetailsRepository.findById(detailId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết hợp đồng với ID: " + detailId));
    }

    public void createContractDetail(ContractDetails contractDetail) {
        contractDetailsRepository.save(contractDetail);
        logger.log(Level.INFO, "Đã tạo chi tiết hợp đồng với ID: " + contractDetail.getContractDetailId());
    }

    public void updateContractDetail(ContractDetails contractDetail) {
        contractDetailsRepository.update(contractDetail);
        logger.log(Level.INFO, "Đã cập nhật chi tiết hợp đồng với ID: " + contractDetail.getContractDetailId());
    }

    public void deleteContractDetail(int detailId) {
        contractDetailsRepository.deleteById(detailId);
        logger.log(Level.INFO, "Đã xóa chi tiết hợp đồng với ID: " + detailId);
    }
}
