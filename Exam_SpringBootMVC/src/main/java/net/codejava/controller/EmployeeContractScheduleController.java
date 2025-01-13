package net.codejava.controller;

import net.codejava.model.EmployeeContractSchedule;
import net.codejava.service.EmployeeContractScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees/schedules")
public class EmployeeContractScheduleController {

    @Autowired
    private EmployeeContractScheduleService employeeScheduleService;

    // API để tạo lịch làm việc cho nhân viên trong một tháng
    @PostMapping("/create/{employeeId}")
    public List<EmployeeContractSchedule> createSchedule(
            @PathVariable int employeeId,
            @RequestParam int month,
            @RequestParam int year) {
        // Gọi dịch vụ để tạo lịch làm việc cho nhân viên trong tháng
        return employeeScheduleService.createScheduleForEmployee(employeeId, month, year);
    }
}
