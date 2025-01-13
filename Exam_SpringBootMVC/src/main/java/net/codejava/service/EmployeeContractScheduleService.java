package net.codejava.service;

import net.codejava.model.EmployeeContractSchedule;
import net.codejava.repository.EmployeeContractScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeContractScheduleService {

    @Autowired
    private EmployeeContractScheduleRepository scheduleRepository;

    // Tạo lịch làm việc cho nhân viên trong tháng
    public List<EmployeeContractSchedule> createScheduleForEmployee(int employeeId, int month, int year) {
        List<EmployeeContractSchedule> schedules = new ArrayList<>();

        // Tạo ngày bắt đầu và kết thúc trong tháng
        LocalDate startDate = LocalDate.of(year, month, 1); // Ngày đầu tháng
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth()); // Ngày cuối tháng

        // Duyệt qua tất cả các ngày trong tháng và tạo lịch làm việc cho từng ngày
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            schedules.addAll(createDailySchedule(employeeId, date));
        }

        // Lưu tất cả các lịch vào cơ sở dữ liệu
        // Phương thức insertBatch() trả về int[], không phải List<EmployeeContractSchedule>
        int[] result = scheduleRepository.insertBatch(schedules);

        // Chỉ cần trả về danh sách lịch đã tạo
        return schedules;
    }

    // Tạo lịch làm việc cho một ngày với các khung giờ cố định
    private List<EmployeeContractSchedule> createDailySchedule(int employeeId, LocalDate date) {
        List<EmployeeContractSchedule> dailySchedules = new ArrayList<>();
        
        // Các khung giờ cố định
        String[] timeSlots = {"06:00-08:00", "08:00-10:00", "10:00-12:00", "14:00-16:00", "16:00-18:00"};
        
        for (String timeSlot : timeSlots) {
            String[] times = timeSlot.split("-");
            LocalDateTime startTime = LocalDateTime.of(date, java.time.LocalTime.parse(times[0]));
            LocalDateTime endTime = LocalDateTime.of(date, java.time.LocalTime.parse(times[1]));

            EmployeeContractSchedule schedule = new EmployeeContractSchedule();
            schedule.setEmployeeId(employeeId);
            schedule.setWorkDate(date);
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            schedule.setStatus(0); // Trạng thái mặc định là chưa có lịch (0)

            dailySchedules.add(schedule);
        }

        return dailySchedules;
    }
}
