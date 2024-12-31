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

    public ContractDetails updateContractDetail(ContractDetails contractDetail) {
        // Lấy chi tiết hợp đồng hiện tại
        ContractDetails currentContractDetail = contractDetailsRepository.getContractDetailById(contractDetail.getContractDetailId());

        // Kiểm tra nếu contractDetail không hợp lệ (null hoặc contractDetailId không tồn tại)
        if (currentContractDetail == null) {
            return null;  // Trả về null nếu không tìm thấy contractDetail
        }

        // Nếu empServiceId thay đổi, xử lý tạo mới bản ghi và cập nhật trạng thái cũ
        if (currentContractDetail.getEmpServiceId() != contractDetail.getEmpServiceId()) {
            // Cập nhật trạng thái của bản ghi cũ thành "đã thay thế" (status = 2)
            currentContractDetail.setStatus(2);
            contractDetailsRepository.updateContractDetail(currentContractDetail);  // Cập nhật bản ghi cũ

            // Tạo bản ghi mới cho chi tiết hợp đồng với empServiceId mới
            contractDetail.setStatus(0);  // Trạng thái của bản ghi mới (hoạt động)
            contractDetailsRepository.createContractDetail(contractDetail);  // Lưu bản ghi mới vào cơ sở dữ liệu

            return contractDetail;  // Trả về bản ghi hợp đồng mới
        } else {
            // Nếu empServiceId không thay đổi, chỉ cần cập nhật thông tin còn lại
            currentContractDetail.setServicePhone(contractDetail.getServicePhone());
            currentContractDetail.setServiceAddress(contractDetail.getServiceAddress());
            contractDetailsRepository.updateContractDetail(currentContractDetail);  // Cập nhật bản ghi hiện tại

            return currentContractDetail;  // Trả về bản ghi hợp đồng đã được cập nhật
        }
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
