package net.codejava.service;

import net.codejava.model.Contracts;
import net.codejava.repository.ContractRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

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
    
    public List<Contracts> getContractsWithPagination(int page, int size, Timestamp startDate, Timestamp endDate) {
        return contractRepository.getContractsWithPagination(page, size, startDate, endDate);
    }

    
    public int getTotalPages(int size, Timestamp startDate, Timestamp endDate) {
        int totalContracts = contractRepository.countContracts(startDate, endDate);
        return (int) Math.ceil((double) totalContracts / size);
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
    	String uploadDirectory = System.getProperty("user.dir") + "/uploads/contracts"; 
    	System.out.println("Upload directory: " + uploadDirectory);


        try {
            // Kiểm tra nếu thư mục chưa tồn tại thì tạo mới
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Created upload directory: " + uploadPath);
            }
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String newFileName = contractId + "_" + timestamp + ".pdf";
            Path filePath = uploadPath.resolve(newFileName);
            System.out.println("Saving file to: " + filePath);

            file.transferTo(filePath);
            
            if (Files.exists(filePath)) {
                System.out.println("File saved successfully.");
            }

            Contracts contract = contractRepository.getContractById(contractId);
            if (contract != null) {
                contract.setContractFile(newFileName); 
                contractRepository.updateContract(contract); 
                System.out.println("Contract file name updated in DB.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while uploading the contract file.", e);
        }
    }


}
