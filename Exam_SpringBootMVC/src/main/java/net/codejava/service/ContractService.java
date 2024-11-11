package net.codejava.service;

import net.codejava.model.Contracts;
import net.codejava.repository.ContractRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    public int createContract(Contracts contract, double servicePrice) {
        return contractRepository.createContract(contract, servicePrice);
    }
    
    public List<Contracts> getAllContracts() {
        return contractRepository.getAllContracts();
    }
    
    public List<Contracts> getContractsByCustomerId(int customerId) {
        return contractRepository.findContractsByCustomerId(customerId);
    }

    public Contracts getContractById(int contractId) {
        return contractRepository.getContractById(contractId);
    }

    public void updateContractStatus(int contractId, int contractStatus) {
        contractRepository.updateContractStatus(contractId, contractStatus);
    }


    
    public void deleteContract(int contractId) {
        contractRepository.deleteContract(contractId);
    }
    


    // Phương thức upload file hợp đồng
    public void uploadContractFile(MultipartFile file, int contractId) {
        // Đường dẫn thư mục lưu trữ file hợp đồng
        String uploadDirectory = "C:/uploads/contracts"; // Đảm bảo đường dẫn này đúng

        try {
            // Kiểm tra nếu thư mục chưa tồn tại thì tạo mới
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Lưu file vào thư mục
            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            // Lưu thông tin vào cơ sở dữ liệu nếu cần
            // Nếu bạn có bảng trong DB để lưu tên file, bạn có thể thực hiện tại đây.
            // Ví dụ: cập nhật thông tin file cho hợp đồng
            Contracts contract = contractRepository.getContractById(contractId);
            if (contract != null) {
                // Giả sử bạn có trường để lưu tên file trong bảng contracts
                contract.setContractFile(file.getOriginalFilename()); // Ví dụ: set tên file
                contractRepository.updateContract(contract); // Cập nhật thông tin vào DB
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while uploading the contract file.", e);
        }
    }


}
