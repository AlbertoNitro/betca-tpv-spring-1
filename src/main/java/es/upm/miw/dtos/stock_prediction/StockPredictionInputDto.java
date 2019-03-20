package es.upm.miw.dtos.stock_prediction;

import es.upm.miw.exceptions.BadRequestException;

public class StockPredictionInputDto {
    private String articleCode;
    private PeriodicityType periodicityType;
    private int periodsNumber;

    public StockPredictionInputDto(String articleCode, PeriodicityType periodicityType, int periodsNumber) {
        this.articleCode = articleCode;
        this.periodicityType = periodicityType;
        this.periodsNumber = periodsNumber;
    }

    public void validate() throws BadRequestException {
        if (!periodicityType.isValidPeriodsNumber(periodsNumber)) {
            throw new BadRequestException(
                    String.format(
                            "PeriodicityType '%s' only support PeriodsNumber values between %d and %d, but current PeriodsNumber was %d",
                            periodicityType,
                            periodicityType.minPeriodsNumberSupport(),
                            periodicityType.maxPeriodsNumberSupport(),
                            periodsNumber));
        }
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public PeriodicityType getPeriodicityType() {
        return periodicityType;
    }

    public void setPeriodicityType(PeriodicityType periodicityType) {
        this.periodicityType = periodicityType;
    }

    public int getPeriodsNumber() {
        return periodsNumber;
    }

    public void setPeriodsNumber(int periodsNumber) {
        this.periodsNumber = periodsNumber;
    }

    @Override
    public String toString() {
        return "StockPredictionInputDto{" +
                "articleCode='" + articleCode + '\'' +
                ", periodicityType=" + periodicityType +
                ", periodsNumber=" + periodsNumber +
                '}';
    }

}