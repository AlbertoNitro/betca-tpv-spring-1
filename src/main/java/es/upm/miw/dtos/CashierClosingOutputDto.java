package es.upm.miw.dtos;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CashierClosingOutputDto {

    private BigDecimal totalVoucher;

    private BigDecimal totalCard;

    private BigDecimal totalCash;

    private BigDecimal salesTotal;

    public CashierClosingOutputDto() {
        // Empty for framework
    }

    public CashierClosingOutputDto(BigDecimal totalCash, BigDecimal totalCard, BigDecimal totalVoucher, BigDecimal salesTotal) {
        this.totalCash = totalCash;
        this.totalCard = totalCard;
        this.totalVoucher = totalVoucher;
        this.salesTotal = salesTotal;
    }

    public BigDecimal getTotalCard() {
        return totalCard.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTotalCard(BigDecimal totalCard) {
        this.totalCard = totalCard;
    }

    public BigDecimal getTotalCash() {
        return totalCash.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTotalCash(BigDecimal totalCash) {
        this.totalCash = totalCash;
    }

    public BigDecimal getTotalVoucher() {
        return totalVoucher.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTotalVoucher(BigDecimal totalVoucher) {
        this.totalVoucher = totalVoucher;
    }

    public BigDecimal getSalesTotal() {
        return salesTotal.setScale(2, RoundingMode.HALF_UP);
    }

    public void setSalesTotal(BigDecimal salesTotal) {
        this.salesTotal = salesTotal;
    }

    @Override
    public String toString() {
        return "CashierClosingOutputDto [getTotalCard()=" + getTotalCard() + ", getTotalCash()=" + getTotalCash() + ", getTotalVoucher()="
                + getTotalVoucher() + ", getSalesTotal()=" + getSalesTotal() + "]";
    }

}
