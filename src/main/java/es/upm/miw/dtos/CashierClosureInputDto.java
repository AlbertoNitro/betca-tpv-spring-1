package es.upm.miw.dtos;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CashierClosureInputDto {

    @NotNull
    private BigDecimal finalCash;

    @NotNull
    private BigDecimal salesCard;

    private String comment;

    public CashierClosureInputDto() {
        // Empty for framework
    }

    public CashierClosureInputDto(BigDecimal finalCash, BigDecimal salesCard, String comment) {
        this.finalCash = finalCash;
        this.salesCard = salesCard;
        this.comment = comment;
    }

    public BigDecimal getFinalCash() {
        return finalCash;
    }

    public void setFinalCash(BigDecimal finalCash) {
        this.finalCash = finalCash;
    }

    public BigDecimal getSalesCard() {
        return salesCard;
    }

    public void setSalesCard(BigDecimal salesCard) {
        this.salesCard = salesCard;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CashierClosureDto [finalCash=" + finalCash + ", salesCard=" + salesCard + ", comment=" + comment + "]";
    }

}
