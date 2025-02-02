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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("admin/employees/schedules")
public class EmployeeContractScheduleController {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeContractScheduleController.class);  
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
    public ResponseEntity<?> createScheduleForAllEmployees(@RequestParam("month") int month, @RequestParam("year") int year) {
        try {
            boolean scheduleExists = employeeScheduleService.isScheduleExist(month, year);
            if (scheduleExists) {
                logger.info("The schedule has been created for the month: " + month + ", year: " + year);
                return ResponseEntity.status(400).body("The schedule has already been created for the month " + month + " year " + year);
            }

            logger.debug("Creating schedule for the month:  " + month + ", year: " + year);
            employeeScheduleService.createSchedulesForAllEmployees(month, year);

            logger.info("The schedule has been created for all employees.");
            return ResponseEntity.ok("The schedule has been created for all employees.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }


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
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/update/{scheduleId}")
    public ResponseEntity<?> updateSchedule(
            @PathVariable int scheduleId,
            @RequestBody EmployeeContractSchedule updatedSchedule) {
        try {
            Optional<EmployeeContractSchedule> existingSchedule = employeeScheduleService.getScheduleById(scheduleId);

            if (existingSchedule.isEmpty()) {
                return ResponseEntity.status(404).body("No schedule found with ID " + scheduleId);
            }

            EmployeeContractSchedule schedule = existingSchedule.get();
            schedule.setStartTime(updatedSchedule.getStartTime());
            schedule.setEndTime(updatedSchedule.getEndTime());
            schedule.setStatus(updatedSchedule.getStatus());

            employeeScheduleService.saveSchedule(schedule);

            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/off/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable int scheduleId) {
        try {
            Optional<EmployeeContractSchedule> schedule = employeeScheduleService.getScheduleById(scheduleId);

            if (schedule.isEmpty()) {
                return ResponseEntity.status(404).body("No schedule found with ID " + scheduleId);
            }

            EmployeeContractSchedule existingSchedule = schedule.get();
            existingSchedule.setStatus(0);  

            employeeScheduleService.saveSchedule(existingSchedule);

            return ResponseEntity.ok("The schedule with ID " + scheduleId + " has been updated with the new status.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
}
