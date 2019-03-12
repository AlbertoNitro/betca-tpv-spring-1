package es.upm.miw.dtos;

import es.upm.miw.documents.Shopping;
import es.upm.miw.documents.Ticket;
import es.upm.miw.documents.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

public class TicketDto {

    private String id;

    private LocalDateTime creationDate;

    private String reference;

    private Shopping[] shoppingList;

    private BigDecimal cash;

    private BigDecimal card;

    private BigDecimal voucher;

    private String note;

    private User user;

    public TicketDto(Ticket ticket) {
        this.id = ticket.getId();
        this.creationDate = ticket.getCreationDate();
        this.reference = ticket.getReference();
        this.shoppingList = ticket.getShoppingList();
        this.cash = ticket.getCash();
        this.card = ticket.getCard();
        this.voucher = ticket.getVoucher();
        this.note = ticket.getNote();
        this.user = ticket.getUser();
    }

    public TicketDto(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Shopping[] getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(Shopping[] shoppingList) {
        this.shoppingList = shoppingList;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getCard() {
        return card;
    }

    public void setCard(BigDecimal card) {
        this.card = card;
    }

    public BigDecimal getVoucher() {
        return voucher;
    }

    public void setVoucher(BigDecimal voucher) {
        this.voucher = voucher;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TicketDto{" +
                "id='" + id + '\'' +
                ", creationDate=" + creationDate +
                ", reference='" + reference + '\'' +
                ", shoppingList=" + Arrays.toString(shoppingList) +
                ", cash=" + cash +
                ", card=" + card +
                ", voucher=" + voucher +
                ", note='" + note + '\'' +
                ", user=" + user +
                '}';
    }
}
