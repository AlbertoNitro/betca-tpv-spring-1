package es.upm.miw.dtos.stock_prediction;

public enum PeriodicityType {
    WEEKLY(1, 26),
    MONTHLY(1, 12),
    YEARLY(1, 2);

    private int minPeriodsNumberSupport;
    private int maxPeriodsNumberSupport;

    private PeriodicityType(int minPeriodsNumberSupport, int maxPeriodsNumberSupport) {
        this.minPeriodsNumberSupport = minPeriodsNumberSupport;
        this.maxPeriodsNumberSupport = maxPeriodsNumberSupport;
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
}