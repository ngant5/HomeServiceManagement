package net.codejava.service;

import net.codejava.model.EmployeeContractSchedule;
import net.codejava.repository.EmployeeContractScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        scheduleRepository.insertBatch(schedules);  // Giả sử insertBatch có thể thực hiện tốt

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
    public EmployeeContractSchedule saveSchedule(EmployeeContractSchedule schedule) {
        return scheduleRepository.save(schedule);  // Bạn cần có phương thức save trong repository để hỗ trợ
    }

    // Xóa lịch làm việc (cập nhật trạng thái thành "đã xóa")
    public void deleteSchedule(int scheduleId) {
        Optional<EmployeeContractSchedule> schedule = getScheduleById(scheduleId);
        if (schedule.isPresent()) {
            EmployeeContractSchedule existingSchedule = schedule.get();
            existingSchedule.setStatus(2); // Giả sử 2 là trạng thái "đã xóa"
            saveSchedule(existingSchedule);
        }
    }
}
