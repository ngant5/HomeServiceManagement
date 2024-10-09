package net.codejava.service;

import net.codejava.model.Contracts;
import net.codejava.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository; 

    public List<Contracts> getAllContracts() {
        return contractRepository.findAll(); 
    }

    public Contracts getContractById(int contractId) {
        return contractRepository.findById(contractId); 
    }

}
