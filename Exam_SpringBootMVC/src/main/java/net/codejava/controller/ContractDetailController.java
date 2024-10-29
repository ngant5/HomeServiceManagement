package net.codejava.controller;

import net.codejava.model.ContractDetails;
import net.codejava.service.ContractDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contract-details")
public class ContractDetailController {

    @Autowired
    private ContractDetailService contractDetailService;

    // Lấy tất cả chi tiết hợp đồng
    @GetMapping
    public ResponseEntity<List<ContractDetails>> getAllContractDetails() {
        List<ContractDetails> details = contractDetailService.getAllContractDetails();
        return ResponseEntity.ok(details);
    }

    // Lấy chi tiết hợp đồng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ContractDetails> getContractDetailById(@PathVariable("id") int id) {
        ContractDetails detail = contractDetailService.getContractDetailById(id);
        return ResponseEntity.ok(detail);
    }

    // Tạo chi tiết hợp đồng mới
    @PostMapping
    public ResponseEntity<ContractDetails> createContractDetail(@RequestBody ContractDetails contractDetail) {
        contractDetailService.createContractDetail(contractDetail);
        return ResponseEntity.status(201).body(contractDetail);
    }

    // Cập nhật chi tiết hợp đồng
    @PutMapping("/{id}")
    public ResponseEntity<ContractDetails> updateContractDetail(@PathVariable("id") int id, @RequestBody ContractDetails contractDetail) {
        contractDetail.setContractDetailId(id); // Set ID để cập nhật
        contractDetailService.updateContractDetail(contractDetail);
        return ResponseEntity.ok(contractDetail);
    }

    // Xóa chi tiết hợp đồng
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContractDetail(@PathVariable("id") int id) {
        contractDetailService.deleteContractDetail(id);
        return ResponseEntity.noContent().build();
    }
}
