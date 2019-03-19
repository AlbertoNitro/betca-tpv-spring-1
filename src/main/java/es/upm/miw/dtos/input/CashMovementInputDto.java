package es.upm.miw.dtos.input;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CashMovementInputDto {

    @NotNull
    private BigDecimal cash;

    @NotNull
    private String comment;

    public CashMovementInputDto() {
        // Empty for framework
    }

    public CashMovementInputDto(BigDecimal cash, String comment) {
        this.cash = cash;
        this.comment = comment;
    }

    public BigDecimal getCash() { return cash; }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CashMovementInputDto{" +
                "cash=" + cash +
                ", comment='" + comment + '\'' +
                '}';
    }
}
