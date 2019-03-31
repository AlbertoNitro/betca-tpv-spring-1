package es.upm.miw.dtos.input;

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

    public void validate() {
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

    public PeriodicityType getPeriodicityType() {
        return periodicityType;
    }

    public int getPeriodsNumber() {
        return periodsNumber;
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