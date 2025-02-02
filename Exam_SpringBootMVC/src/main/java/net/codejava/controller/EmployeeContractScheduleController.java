package net.codejava.controller;

import net.codejava.model.EmployeeContractSchedule;
import net.codejava.model.Employees;
import net.codejava.service.EmployeeContractScheduleService;
import net.codejava.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin/employees/schedules")
public class EmployeeContractScheduleController {

    @Autowired
    private EmployeeContractScheduleService employeeScheduleService;
    
    @Autowired
    private EmployeeService employeeService;
    
    private static final String[] timeSlots = {"06:00-08:00", "08:00-10:00", "10:00-12:00", "14:00-16:00", "16:00-18:00"};

 	@GetMapping("/add")
 	public String showAddSchedulePage() {
       	return "admin/ad_addschedule"; 
	}
   
    
    @PostMapping("/createForAllEmployees")
    public ResponseEntity<?> createScheduleForAllEmployees() {
        try {
            LocalDate currentDate = LocalDate.now();
            int currentMonth = currentDate.getMonthValue();
            int currentYear = currentDate.getYear() + 1; // Tăng năm lên 1

            // Tạo lịch cho tất cả nhân viên trong tháng hiện tại và năm sau
            employeeScheduleService.createSchedulesForAllEmployees(currentMonth, currentYear);

            return ResponseEntity.ok("Đã tạo lịch làm việc cho tất cả nhân viên.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Đã có lỗi xảy ra: " + e.getMessage());
        }
    }


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

    // API để cập nhật lịch làm việc của nhân viên
    @PutMapping("/update/{scheduleId}")
    public ResponseEntity<?> updateSchedule(
            @PathVariable int scheduleId,
            @RequestBody EmployeeContractSchedule updatedSchedule) {
        try {
            // Lấy lịch cũ từ cơ sở dữ liệu
            Optional<EmployeeContractSchedule> existingSchedule = employeeScheduleService.getScheduleById(scheduleId);

            if (existingSchedule.isEmpty()) {
                return ResponseEntity.status(404).body("Không tìm thấy lịch làm việc với ID " + scheduleId);
            }

            // Cập nhật thông tin lịch làm việc
            EmployeeContractSchedule schedule = existingSchedule.get();
            schedule.setStartTime(updatedSchedule.getStartTime());
            schedule.setEndTime(updatedSchedule.getEndTime());
            schedule.setStatus(updatedSchedule.getStatus());

            // Lưu lại lịch đã cập nhật
            employeeScheduleService.saveSchedule(schedule);

            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Đã có lỗi xảy ra: " + e.getMessage());
        }
    }

    // API để cập nhật trạng thái (xóa) lịch làm việc của nhân viên
    @PutMapping("/delete/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable int scheduleId) {
        try {
            // Lấy lịch từ cơ sở dữ liệu
            Optional<EmployeeContractSchedule> schedule = employeeScheduleService.getScheduleById(scheduleId);

            if (schedule.isEmpty()) {
                return ResponseEntity.status(404).body("Không tìm thấy lịch làm việc với ID " + scheduleId);
            }

            // Cập nhật trạng thái của lịch làm việc thành "đã xóa" (ví dụ: 2 hoặc 3 - tùy vào quy định của bạn)
            EmployeeContractSchedule existingSchedule = schedule.get();
            existingSchedule.setStatus(2);  // Giả sử trạng thái 2 là "đã xóa"

            // Lưu lại lịch đã cập nhật trạng thái
            employeeScheduleService.saveSchedule(existingSchedule);

            return ResponseEntity.ok("Lịch làm việc với ID " + scheduleId + " đã được cập nhật trạng thái.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Đã có lỗi xảy ra: " + e.getMessage());
        }
    }
}
