package net.codejava.service;

import net.codejava.model.ContractDetails;
import net.codejava.model.Contracts;
import net.codejava.model.EmployeeServices;
import net.codejava.repository.ContractRepository;
import net.codejava.repository.EmployeeServicesRepository; 
import net.codejava.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ContractService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ContractRepository contractRepository; 
    
    @Autowired
    private EmployeeServicesRepository employeeServiceRepository; 

    private static final Logger logger = Logger.getLogger(ContractService.class.getName());

    
    
    public List<Contracts> getAllContracts() {
        return contractRepository.findAll(); 
    }

    public Contracts getContractById(int contractId) {
        return contractRepository.findById(contractId); 
    }
    
    public Contracts createContract(Contracts contract, List<ContractDetails> details) {
    	if (contract.getCreatedAt() == null) {
            contract.setCreatedAt(LocalDateTime.now());
        }
        Contracts savedContract = contractRepository.createContract(contract);
        contractRepository.saveContractDetails(savedContract.getContractId(), details);
        return savedContract;
    }

    public double getServicePrice(int serviceId) {
        return serviceRepository.getServicePrice(serviceId);
    }
    
    public double calculateTotalPrice(List<Integer> serviceIds) {
        return serviceIds.stream().mapToDouble(this::getServicePrice).sum();
    }
}
