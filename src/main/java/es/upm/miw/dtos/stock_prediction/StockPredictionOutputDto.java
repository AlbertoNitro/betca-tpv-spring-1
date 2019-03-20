package es.upm.miw.dtos.stock_prediction;

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

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(int periodNumber) {
        this.periodNumber = periodNumber;
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