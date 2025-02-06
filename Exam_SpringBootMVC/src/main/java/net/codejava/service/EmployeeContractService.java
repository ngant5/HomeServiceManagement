package net.codejava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.codejava.model.Contracts;
import net.codejava.repository.EmployeeContractRepository;

import java.sql.Timestamp;
import java.util.List;

@Service
public class EmployeeContractService {

    @Autowired
    private EmployeeContractRepository contractRepository;

    public List<Contracts> getContractsByEmployeeIdWithPagination(int employeeId, int page, int size, Timestamp startDate, Timestamp endDate) {
        return contractRepository.getContractsByEmployeeIdWithPagination(employeeId, page, size, startDate, endDate);
    }

    public int getTotalPagesForEmployee(int employeeId, int size, Timestamp startDate, Timestamp endDate) {
        int totalContracts = contractRepository.countContractsByEmployee(employeeId, startDate, endDate);
        return (int) Math.ceil((double) totalContracts / size);
    }
    
    public void updateContractStatus(int contractId, int contractStatus) {
        contractRepository.updateContractStatus(contractId, contractStatus);
    }
}
