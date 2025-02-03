package net.codejava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import net.codejava.model.EmployeeContractSchedule;
import net.codejava.repository.EmployeeContractScheduleRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component  
public class SchedulingConfig {

    @Autowired
    private EmployeeContractScheduleRepository scheduleRepository;
    
    public SchedulingConfig(EmployeeContractScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Scheduled(cron = "0 */15 * * * ?") 
    public void updateExpiredSlots() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Scheduled job running at: " + now);

        List<EmployeeContractSchedule> selectedSlots = scheduleRepository.findByStatusAndExpireTimeIsNotNull(1);
        
        if (selectedSlots.isEmpty()) {
            System.out.println("No selected slots found with status = 1.");
        }

        for (EmployeeContractSchedule schedule : selectedSlots) {
            System.out.println("Checking scheduleId: " + schedule.getScheduleId() + " with expire_time: " + schedule.getExpireTime());

            if (schedule.getExpireTime() != null && schedule.getStatus() == 1) {
                if (schedule.getExpireTime().isBefore(now)) {
                    scheduleRepository.updateStatusRemove(schedule.getScheduleId(), 0, null);
                    System.out.println("Slot with scheduleId " + schedule.getScheduleId() + " has expired and was updated.");
                }
            }
        }
    }
}
