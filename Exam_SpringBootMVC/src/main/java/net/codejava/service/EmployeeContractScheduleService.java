package net.codejava.service;

import net.codejava.model.EmployeeContractSchedule;
import net.codejava.model.EmployeeSchedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.codejava.model.Employees;
import net.codejava.repository.EmployeeContractScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeContractScheduleService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeContractScheduleService.class);

    @Autowired
    private EmployeeContractScheduleRepository scheduleRepository; 
    
    @Autowired
    private EmployeeService employeeService;
    
    public boolean isScheduleExist(int month, int year) {
        return scheduleRepository.existsByMonthAndYear(month, year);
    }

    
    public void createSchedulesForAllEmployees(int month, int year) {
        List<Employees> employees = employeeService.getAllEmployees();
        
        for (Employees employee : employees) {
            createScheduleForEmployee(employee.getEmployeeId(), month, year);
        }
    }

    public List<EmployeeContractSchedule> createScheduleForEmployee(int employeeId, int month, int year) {
        List<EmployeeContractSchedule> schedules = new ArrayList<>();

        LocalDate startDate = LocalDate.of(year, month, 1); 
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth()); 

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            schedules.addAll(createDailySchedule(employeeId, date));
        }

        scheduleRepository.insertBatch(schedules);  

        return schedules;
    }
    
    

    private List<EmployeeContractSchedule> createDailySchedule(int employeeId, LocalDate date) {
        List<EmployeeContractSchedule> dailySchedules = new ArrayList<>();

        String[] timeSlots = {"06:00-08:00", "08:00-10:00", "10:00-12:00", "14:00-16:00", "16:00-18:00"};

        for (String timeSlot : timeSlots) {
            String[] times = timeSlot.split("-");
            LocalTime startTime = LocalTime.parse(times[0]);  
            LocalTime endTime = LocalTime.parse(times[1]);  

            EmployeeContractSchedule schedule = new EmployeeContractSchedule();
            schedule.setEmployeeId(employeeId);
            schedule.setWorkDate(date);
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            schedule.setStatus(0); 
            schedule.setHoursWork(2); 

            dailySchedules.add(schedule);
        }

        return dailySchedules;
    }

    // Phương thức tạo lịch làm việc cho tất cả nhân viên vào đầu mỗi tháng
    @Scheduled(cron = "0 0 1 * * ?")  // Chạy vào lúc 00:00 ngày 1 của mỗi tháng
    public void createSchedulesForAllEmployees() {
        // Lấy danh sách tất cả nhân viên từ cơ sở dữ liệu
        List<Integer> employeeIds = getAllEmployeeIds();

        // Tạo lịch cho từng nhân viên vào đầu mỗi tháng
        for (int employeeId : employeeIds) {
            LocalDate currentDate = LocalDate.now();  // Lấy ngày hiện tại
            int currentMonth = currentDate.getMonthValue();
            int currentYear = currentDate.getYear();

            // Tạo lịch cho nhân viên ở tháng hiện tại
            createScheduleForEmployee(employeeId, currentMonth, currentYear);
        }

        System.out.println("Lịch làm việc đã được tạo cho tất cả nhân viên vào đầu tháng.");
    }


    // Phương thức giả định để lấy tất cả các ID nhân viên (bạn có thể thay thế bằng phương thức thực tế)
    private List<Integer> getAllEmployeeIds() {
        return scheduleRepository.findAllEmployeeIds();  // Phương thức này cần được implement trong Repository
    }

    public List<EmployeeContractSchedule> getSchedulesByEmployeeAndDate(int employeeId, LocalDate date) {
        return scheduleRepository.findByEmployeeIdAndWorkDate(employeeId, date);
    }

    // Lấy lịch làm việc theo ID
    public Optional<EmployeeContractSchedule> getScheduleById(int scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }

    // Lưu lại lịch làm việc (cập nhật hoặc thêm mới)
    public EmployeeContractSchedule saveSchedule(EmployeeContractSchedule
    		 schedule) { return scheduleRepository.save(schedule);
    		 }

    
    public List<EmployeeContractSchedule> getSchedulesByEmployeeAndStatus(int employeeId, int status) {
        return scheduleRepository.findByEmployeeIdAndStatus(employeeId, status);
    }

    public List<EmployeeContractSchedule> getSchedulesByEmployeeId(int employeeId) {
        return scheduleRepository.findByEmployeeIdAndStatus(employeeId, 0);  // Giả sử status = 0 là lịch trình hợp lệ
    }

    public boolean updateSlotStatus(int scheduleId, int status, LocalDateTime expireTime) {
        try {
            Optional<EmployeeContractSchedule> scheduleOpt = scheduleRepository.findById(scheduleId);
            
            if (scheduleOpt.isPresent()) {
            	EmployeeContractSchedule schedule = scheduleOpt.get();
                schedule.setStatus(status);

                if (expireTime == null) {
                    expireTime = LocalDateTime.now();
                }
                schedule.setExpireTime(expireTime);

                scheduleRepository.updateStatus(scheduleId, status, expireTime);

                return true;
            } else {
                System.out.println("No schedule found with scheduleId: " + scheduleId);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateContractDetailIdAndStatus(int scheduleId, int contractDetailId, int status) {
        try {
            return scheduleRepository.updateContractDetailIdAndStatus(scheduleId, contractDetailId, status);
        } catch (Exception e) {
            logger.error("Error updating contractDetailId and status for scheduleId: {}", scheduleId, e);
            return false;
        }
    }
    
    public List<EmployeeSchedule> getSchedulesByDate(LocalDate workDate) {
        return scheduleRepository.findSchedulesByWorkDate(workDate);
    }

    public boolean updateScheduleStatus(int scheduleId, int newStatus) {
        return scheduleRepository.updateScheduleStatus(scheduleId, newStatus);
    }

}