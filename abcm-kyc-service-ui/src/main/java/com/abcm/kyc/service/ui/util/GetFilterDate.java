package com.abcm.kyc.service.ui.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import com.abcm.kyc.service.ui.dto.DateRange;

public class GetFilterDate {

    public static DateRange getDateRange(String filterType) {
        LocalDate today = LocalDate.now();

        switch (filterType.toLowerCase()) {
            case "today":
                return new DateRange(today, today);

            case "weekly":
                LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
                LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
                return new DateRange(startOfWeek, endOfWeek);

            case "monthly":
                LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
                return new DateRange(startOfMonth, endOfMonth);

            case "lastmonth":
                // Calculate start and end of last month
                LocalDate firstDayOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                LocalDate lastDayOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
                return new DateRange(firstDayOfLastMonth, lastDayOfLastMonth);

            default:
                throw new IllegalArgumentException("Invalid filter type. Use 'today', 'weekly', 'monthly', or 'lastmonth'.");
        }
    }
}
