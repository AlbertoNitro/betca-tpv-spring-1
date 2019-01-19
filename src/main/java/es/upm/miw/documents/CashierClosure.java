package es.upm.miw.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document
public class CashierClosure {

    @Id
    private String id;

    private LocalDateTime openingDate;

    private BigDecimal initialCash;

    private BigDecimal usedVouchers;

    private BigDecimal salesCard;

    private BigDecimal salesCash;

    private BigDecimal deposit;

    private BigDecimal withdrawal;

    private BigDecimal finalCash;

    private BigDecimal lostCash;

    private String comment;

    private LocalDateTime closureDate;

    public CashierClosure(BigDecimal initialCash) {
        this.openingDate = LocalDateTime.now();
        this.initialCash = initialCash;
        this.closureDate = null;
    }

    public void deposit(BigDecimal cash, String comment) {
        this.deposit = this.deposit.add(cash);
        this.comment += "Deposit (" + cash.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "): "
                + comment + ". ";
    }

    public void withdrawal(BigDecimal cash, String comment) {
        this.withdrawal = this.withdrawal.add(cash);
        this.comment += "Withdrawal (" + cash.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "): "
                + comment + ". ";
    }

    public void close(BigDecimal salesCard, BigDecimal salesCash, BigDecimal usedVouchers, BigDecimal finalCash, String comment) {
        this.salesCard = salesCard;
        this.salesCash = salesCash;
        this.usedVouchers = usedVouchers;
        this.finalCash = finalCash;
        //TODO falta calcular el dinero total que queda despues de cerrar
        this.lostCash = BigDecimal.ZERO;
        this.comment += comment + ". ";
        this.closureDate = LocalDateTime.now();
    }

    public boolean isClosed() {
        return getClosureDate() != null;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public BigDecimal getWithdrawal() {
        return withdrawal;
    }

    public BigDecimal getInitialCash() {
        return initialCash;
    }

    public BigDecimal getUsedVouchers() {
        return usedVouchers;
    }

    public BigDecimal getSalesCash() {
        return salesCash;
    }

    public BigDecimal getSalesCard() {
        return salesCard;
    }

    public BigDecimal getFinalCash() {
        return finalCash;
    }

    public BigDecimal getLostCash() {
        return lostCash;
    }

    public LocalDateTime getOpeningDate() {
        return openingDate;
    }

    public LocalDateTime getClosureDate() {
        return closureDate;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && id.equals(((CashierClosure) obj).id);
    }

    @Override
    public String toString() {
        return "CashierClosure{" +
                "id='" + id + '\'' +
                ", openingDate=" + openingDate +
                ", initialCash=" + initialCash +
                ", usedVouchers=" + usedVouchers +
                ", salesCard=" + salesCard +
                ", salesCash=" + salesCash +
                ", deposit=" + deposit +
                ", withdrawal=" + withdrawal +
                ", finalCash=" + finalCash +
                ", comment='" + comment + '\'' +
                ", closureDate=" + closureDate +
                '}';
    }
}
