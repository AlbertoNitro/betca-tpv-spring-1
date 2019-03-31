package es.upm.miw.dtos.input;

import es.upm.miw.dtos.output.PeriodType;

import java.time.format.DateTimeFormatter;

public enum PeriodicityType {
    WEEKLY(1, 26, PeriodType.WEEK, DateTimeFormatter.ofPattern("yyyy-ww")),
    MONTHLY(1, 12, PeriodType.MONTH, DateTimeFormatter.ofPattern("yyyy-MM")),
    YEARLY(1, 2, PeriodType.YEAR, DateTimeFormatter.ofPattern("yyyy"));

    private int minPeriodsNumberSupport;
    private int maxPeriodsNumberSupport;
    private PeriodType periodType;
    private DateTimeFormatter dateTimeFormatter;

    private PeriodicityType(int minPeriodsNumberSupport, int maxPeriodsNumberSupport, PeriodType periodType, DateTimeFormatter dateTimeFormatter) {
        this.minPeriodsNumberSupport = minPeriodsNumberSupport;
        this.maxPeriodsNumberSupport = maxPeriodsNumberSupport;
        this.periodType = periodType;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public boolean isValidPeriodsNumber(int periodsNumber) {
        return periodsNumber >= minPeriodsNumberSupport() && periodsNumber <= maxPeriodsNumberSupport();
    }

    public int minPeriodsNumberSupport() {
        return minPeriodsNumberSupport;
    }

    public int maxPeriodsNumberSupport() {
        return maxPeriodsNumberSupport;
    }

    public DateTimeFormatter dateTimeFormatter() {
        return dateTimeFormatter;
    }

    public PeriodType periodType() {
        return periodType;
    }
}