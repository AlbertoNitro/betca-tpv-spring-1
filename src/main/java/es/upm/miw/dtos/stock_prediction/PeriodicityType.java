package es.upm.miw.dtos.stock_prediction;

import java.time.format.DateTimeFormatter;

public enum PeriodicityType {
    WEEKLY(1, 26, DateTimeFormatter.ofPattern("yyyy-ww")),
    MONTHLY(1, 12, DateTimeFormatter.ofPattern("yyyy-MM")),
    YEARLY(1, 2, DateTimeFormatter.ofPattern("yyyy"));

    private int minPeriodsNumberSupport;
    private int maxPeriodsNumberSupport;
    private DateTimeFormatter dateTimeFormatter;

    private PeriodicityType(int minPeriodsNumberSupport, int maxPeriodsNumberSupport, DateTimeFormatter dateTimeFormatter) {
        this.minPeriodsNumberSupport = minPeriodsNumberSupport;
        this.maxPeriodsNumberSupport = maxPeriodsNumberSupport;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public int minPeriodsNumberSupport() {
        return this.minPeriodsNumberSupport;
    }

    public int maxPeriodsNumberSupport() {
        return this.maxPeriodsNumberSupport;
    }

    public boolean isValidPeriodsNumber(int periodsNumber) {
        return periodsNumber >= minPeriodsNumberSupport() && periodsNumber <= maxPeriodsNumberSupport();
    }

    public DateTimeFormatter dateTimeFormatter() {
        return this.dateTimeFormatter;
    }
}