package es.upm.miw.documents;

import es.upm.miw.businessServices.Encrypting;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

@Document
public class Ticket {

    private static final String DATE_FORMAT = "yyyyMMdd";

    @Id
    private String id;

    private LocalDateTime creationDate;

    private String reference;

    private Shopping[] shoppingList;

    private BigDecimal cashDeposited;

    private BigDecimal debt;

    private String note;

    @DBRef
    private User user;

    public Ticket() {
        this.creationDate = LocalDateTime.now();
        this.reference = new Encrypting().encryptInBase64UrlSafe();
        this.debt = BigDecimal.ZERO;
        this.note = "";
    }

    public Ticket(int idOfday, BigDecimal cashDeposited, Shopping[] shoppingList, User user) {
        this();
        this.id = new SimpleDateFormat(DATE_FORMAT).format(new Date()) + idOfday;
        this.setCashDeposited(cashDeposited);
        this.shoppingList = shoppingList;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int simpleId() {
        return Integer.parseInt(String.valueOf(id).substring(DATE_FORMAT.length()));
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Shopping[] getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(Shopping[] shoppingList) {
        this.shoppingList = shoppingList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getCashDeposited() {
        return cashDeposited;
    }

    public void setCashDeposited(BigDecimal cashDeposited) {
        this.cashDeposited = cashDeposited.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public void setDebt(BigDecimal debt) {
        if (debt.signum() == -1) {
            this.debt = BigDecimal.ZERO;
        } else {
            this.debt = debt;
        }
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal(0);
        for (Shopping shopping : shoppingList) {
            total = total.add(shopping.getShoppingTotal());
        }
        return total;
    }

    public BigDecimal getTotalCommited() {
        BigDecimal total = new BigDecimal(0);
        for (Shopping shopping : shoppingList) {
            if (ShoppingState.COMMITTED.equals(shopping.getShoppingState())) {
                total = total.add(shopping.getShoppingTotal());
            }
        }
        return total;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && id.equals(((Ticket) obj).id);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Ticket[" + id + ": created=" + creationDate + ", reference=" + reference + ", shoppingList="
                + Arrays.toString(shoppingList) + ", cashDeposited=" + cashDeposited + ", debt: " + this.debt + " , note: " + this.note);
        if (user != null) {
            stringBuilder.append(", userId=" + user.getMobile());
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

}
