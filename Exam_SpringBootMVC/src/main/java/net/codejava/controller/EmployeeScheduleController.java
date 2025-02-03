package net.codejava.controller;

import net.codejava.model.EmployeeContractSchedule;
import net.codejava.service.EmployeeContractScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("schedules/")
public class EmployeeScheduleController {
    
    @Autowired
    private EmployeeContractScheduleService employeeScheduleService;

    // API để lấy lịch làm việc của nhân viên vào một ngày cụ thể
    @GetMapping("/{employeeId}/date")
    public ResponseEntity<?> getSchedulesByEmployeeAndDate(
            @PathVariable int employeeId,
            @RequestParam String date) {
        try {
            // Chuyển đổi workDate từ String thành LocalDate
            LocalDate workDate = LocalDate.parse(date);
            List<EmployeeContractSchedule> schedules = employeeScheduleService.getSchedulesByEmployeeAndDate(employeeId, workDate);

            // Kiểm tra nếu không có lịch
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
}
