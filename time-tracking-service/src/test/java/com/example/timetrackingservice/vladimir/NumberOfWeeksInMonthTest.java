package com.example.timetrackingservice.vladimir;

import com.example.timetrackingservice.service.logic.NumberOfWeeksInAMonth;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberOfWeeksInMonthTest {

    private final NumberOfWeeksInAMonth weekCalculator = new NumberOfWeeksInAMonth();

    @Test
    void shouldReturnCorrectNumberOfWeeksInFebruary2025() {
        int weeks = weekCalculator.countWeeksInAMonth(2025, 2);
        assertEquals(5, weeks);
    }

    @Test
    void shouldReturnCorrectWeekRangesForMarch2025() {
        int weeks = weekCalculator.countWeeksInAMonth(2025, 2);
        assertEquals(5, weeks);
    }

    @Test
    void shouldReturnCorrectWeekRangesForMay2021() {
        int weeks = weekCalculator.countWeeksInAMonth(2021, 5);
        assertEquals(6, weeks);
    }
}
