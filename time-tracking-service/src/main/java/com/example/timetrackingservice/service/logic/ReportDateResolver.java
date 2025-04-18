package com.example.timetrackingservice.service.logic;

import com.example.timetrackingservice.service.logic.enity.DateRange;
import com.example.timetrackingservice.service.logic.enity.ReportType;
import com.example.timetrackingservice.service.logic.enity.WeekRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ReportDateResolver {
    private final NumberOfWeeksInAMonth numberOfWeeksInAMonth;
    private static final Pattern YEAR_PATTERN =
            Pattern.compile("^\\d{4}$");

    private static final Pattern MONTH_PATTERN =
            Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])$");

    private static final Pattern DAY_PATTERN =
            Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$");

    private static final Pattern WEEK_PATTERN =
            Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-([1-6])w$");

    public ReportType resolveReportType(String date) {
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException(buildFormatErrorMessage());
        }

        if (DAY_PATTERN.matcher(date).matches()) {
            return ReportType.DAY;
        }
        if (WEEK_PATTERN.matcher(date).matches()) {
            return ReportType.WEEK;
        }
        if (MONTH_PATTERN.matcher(date).matches()) {
            return ReportType.MONTH;
        }
        if (YEAR_PATTERN.matcher(date).matches()) {
            return ReportType.YEAR;
        }

        throw new IllegalArgumentException(buildFormatErrorMessage());
    }

    public DateRange resolveDateRange(String date) {
        ReportType type = resolveReportType(date);

        switch (type) {
            case DAY:
                LocalDate d = LocalDate.parse(date);
                return new DateRange(d.atStartOfDay(), d.atTime(LocalTime.MAX));

            case WEEK:
                String[] parts = date.replace("w", "").split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int week = Integer.parseInt(parts[2]);

                WeekRange range = numberOfWeeksInAMonth.getWeeksInMonth(year, month).get(week);
                if (range == null) {
                    int totalWeeks = numberOfWeeksInAMonth.countWeeksInAMonth(year, month);
                    throw new IllegalArgumentException("Week " + week + " does not exist for " + month + "/" + year +
                            ". Total weeks in this month: " + totalWeeks);
                }
                return new DateRange(range.start().atStartOfDay(), range.end().atTime(LocalTime.MAX));

            case MONTH:
                YearMonth ym = YearMonth.parse(date);
                return new DateRange(ym.atDay(1).atStartOfDay(), ym.atEndOfMonth().atTime(LocalTime.MAX));

            case YEAR:
                int y = Integer.parseInt(date);
                return new DateRange(
                        LocalDate.of(y, 1, 1).atStartOfDay(),
                        LocalDate.of(y, 12, 31).atTime(LocalTime.MAX)
                );

            default:
                throw new IllegalArgumentException("Unknown report type: " + type);
        }
    }


    public String buildFormatErrorMessage() {
        return """
            Error: incorrect input.
            For year report use: yyyy
            For month report use: yyyy-mm
            For week report use: yyyy-mm-nw (n from 1 to 6)
            For day report use: yyyy-mm-dd
            """;
    }
}
