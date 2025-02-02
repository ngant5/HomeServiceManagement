package net.codejava.controller;

import net.codejava.model.EmployeeContractSchedule;
import net.codejava.service.EmployeeContractScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
}
