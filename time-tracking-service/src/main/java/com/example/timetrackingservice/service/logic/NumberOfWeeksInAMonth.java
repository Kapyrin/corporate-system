package com.example.timetrackingservice.service.logic;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class NumberOfWeeksInAMonth {

    public Map<Integer, WeekRange> getWeeksInMonth(int year, int month) {
        Map<Integer, WeekRange> weeks = new LinkedHashMap<>();

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = YearMonth.of(year, month).atEndOfMonth();

        int weekNumber = 1;

        while (!start.isAfter(endOfMonth)) {
            int daysToAdd;
            if (weekNumber == 1) {
                daysToAdd = 7 - start.getDayOfWeek().getValue();
            } else {
                daysToAdd = 6;
            }

            LocalDate end = start.plusDays(daysToAdd);
            if (end.isAfter(endOfMonth)) {
                end = endOfMonth;
            }

            weeks.put(weekNumber++, new WeekRange(start, end));
            start = end.plusDays(1);
        }

        return weeks;
    }


    public int countWeeksInAMonth(int year, int month) {
        return getWeeksInMonth(year, month).size();
    }
}
