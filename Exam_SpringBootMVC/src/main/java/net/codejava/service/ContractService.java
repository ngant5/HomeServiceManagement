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

    public int createContract(Contracts contract) {
        return contractRepository.createContract(contract);
    }
    
    public List<Contracts> getAllContracts() {
        return contractRepository.getAllContracts();
    }

    public Contracts getContractById(int contractId) {
        return contractRepository.getContractById(contractId);
    }

    public void updateContract(Contracts contract) {
        contractRepository.updateContract(contract);
    }

    public void deleteContract(int contractId) {
        contractRepository.deleteContract(contractId);
    }
}
