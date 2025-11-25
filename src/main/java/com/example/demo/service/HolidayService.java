package com.example.demo.service;

import com.example.demo.model.Holiday;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HolidayService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    private static final Map<LocalDate, String> holidays = new HashMap<>();

    static {
        holidays.put(LocalDate.of(2025, Month.JANUARY, 7), "Christmas");
        holidays.put(LocalDate.of(2025, Month.APRIL, 20), "Easter");
        holidays.put(LocalDate.of(2025, Month.JUNE, 8), "Trinity");
    }

    @Scheduled(cron = "0 0 8 * * *") // Every day at 8 AM
    public void checkForHolidays() {
        LocalDate today = LocalDate.now();
        if (holidays.containsKey(today)) {
            String holidayName = holidays.get(today);
            List<User> allUsers = userRepository.findAll();
            for (User user : allUsers) {
                String message = "Today is " + holidayName;
                notificationService.createNotification(user, message, "HOLIDAY");
            }
        }
    }

    public List<Holiday> getHolidays() {
        List<Holiday> holidayList = new ArrayList<>();
        holidayList.add(new Holiday("2025-01-07", "Рождество", "Рождество"));
        holidayList.add(new Holiday("2025-04-20", "Пасха", "Пасха"));
        holidayList.add(new Holiday("2025-06-08", "Троица", "Троица"));
        return holidayList;
    }
}
