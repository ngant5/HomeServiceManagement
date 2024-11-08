package net.codejava.service;

import net.codejava.model.Contracts;
import net.codejava.repository.ContractRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
