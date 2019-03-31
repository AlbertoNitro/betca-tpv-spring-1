package es.upm.miw.dtos.output;

public class StockPredictionOutputDto {
    private PeriodType periodType;
    private int periodNumber;
    private int stock;

    public StockPredictionOutputDto(PeriodType periodType, int periodNumber, int stock) {
        this.periodType = periodType;
        this.periodNumber = periodNumber;
        this.stock = stock;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "StockPredictionOutputDto{" +
                "periodType=" + periodType +
                ", periodNumber=" + periodNumber +
                ", stock=" + stock +
                '}';
    }
}