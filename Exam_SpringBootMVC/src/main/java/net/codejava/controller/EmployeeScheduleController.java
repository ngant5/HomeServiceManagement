package net.codejava.controller;

import net.codejava.model.EmployeeContractSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.codejava.service.EmployeeContractScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("schedules/")
public class EmployeeScheduleController {
    
	private static final Logger logger = LoggerFactory.getLogger(EmployeeScheduleController.class);

    @Autowired
    private EmployeeContractScheduleService employeeScheduleService;

    @GetMapping("/{employeeId}/date")
    public ResponseEntity<?> getSchedulesByEmployeeAndDate(
            @PathVariable int employeeId,
            @RequestParam String date) {
        try {
            LocalDate workDate = LocalDate.parse(date);
            List<EmployeeContractSchedule> schedules = employeeScheduleService.getSchedulesByEmployeeAndDate(employeeId, workDate);

            if (schedules.isEmpty()) {
                return ResponseEntity.ok("No available schedules for the selected date.");
            }

            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Đã có lỗi xảy ra: " + e.getMessage());
        }
    }
    
    @PutMapping("/updateSlot/{scheduleId}")
    public ResponseEntity<Map<String, Object>> updateSlotStatus(@PathVariable int scheduleId, 
                                    @RequestParam int status, 
                                    @RequestParam(required = false) LocalDateTime expireTime) {
    	Map<String, Object> response = new HashMap<>();
        boolean success = employeeScheduleService.updateSlotStatus(scheduleId, status, expireTime);
        
        if (success) {
            response.put("success", true);
            response.put("message", "Status updated successfully.");
            return ResponseEntity.ok(response); 
        } else {
            response.put("success", false);
            response.put("message", "Failed to update slot status.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); 
        }
    }
    
    @PutMapping("/updateContractDetailId/{scheduleId}")
    public ResponseEntity<Map<String, Object>> updateContractDetailIdAndStatus(
            @PathVariable int scheduleId, 
            @RequestParam int contractDetailId, 
            @RequestParam int status) {
        
        Map<String, Object> response = new HashMap<>();
        boolean success = employeeScheduleService.updateContractDetailIdAndStatus(scheduleId, contractDetailId, status);

        if (success) {
            response.put("success", true);
            response.put("message", "ContractDetailId and Status updated successfully.");
            return ResponseEntity.ok(response); 
        } else {
            response.put("success", false);
            response.put("message", "Failed to update ContractDetailId and Status.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); 
        }
    }
    
    @PostMapping("/updateSchedulesStatus")
    public ResponseEntity<Map<String, Object>> updateSchedulesStatus(
    		@RequestParam String scheduleIds,
            @RequestParam Integer contractDetailId, 
            @RequestParam Integer status) {

        try {
            List<Integer> scheduleIdList = Arrays.stream(scheduleIds.split(","))
                                                 .map(Integer::parseInt)
                                                 .collect(Collectors.toList());
            
            for (Integer scheduleId : scheduleIdList) {
                boolean updateResult = employeeScheduleService.updateContractDetailIdAndStatus(scheduleId, contractDetailId, status);
                if (!updateResult) {
                    logger.error("Failed to update status for scheduleId: {}", scheduleId);
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating schedules status: ", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



}

    		