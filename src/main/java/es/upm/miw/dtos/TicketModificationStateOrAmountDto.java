package es.upm.miw.dtos;

import es.upm.miw.documents.Shopping;
import es.upm.miw.documents.Ticket;
import es.upm.miw.documents.User;

import java.math.BigDecimal;
import java.util.Arrays;


public class TicketModificationStateOrAmountDto {

    private ShoppingModificationStateOrAmountDto[] shoppingList;

    private BigDecimal cash;

    private BigDecimal card;

    private BigDecimal voucher;

    private String note;

    private User user;

    public TicketModificationStateOrAmountDto(Ticket ticket) {
        this.shoppingList = this.initializeShoppingTicket(ticket.getShoppingList());
        this.cash = ticket.getCash();
        this.card = ticket.getCard();
        this.voucher = ticket.getVoucher();
        this.note = ticket.getNote();
        this.user = ticket.getUser();
    }

    private ShoppingModificationStateOrAmountDto[] initializeShoppingTicket(Shopping[] shoppings) {
        ShoppingModificationStateOrAmountDto[] shoppingModificationDto = new ShoppingModificationStateOrAmountDto[shoppings.length];
        for(int i = 0; i > shoppings.length; i++) {
            shoppingModificationDto[i] = new ShoppingModificationStateOrAmountDto(shoppings[i]);
        }
        return shoppingModificationDto;
    }

    public ShoppingModificationStateOrAmountDto[] getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingModificationStateOrAmountDto[] shoppingList) {
        this.shoppingList = shoppingList;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public BigDecimal getCard() {
        return card;
    }

    public BigDecimal getVoucher() {
        return voucher;
    }


    public String getNote() {
        return note;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "TicketModificationStateOrAmountDto{" +
                "shoppingList=" + Arrays.toString(shoppingList) +
                ", cash=" + cash +
                ", card=" + card +
                ", voucher=" + voucher +
                ", note='" + note + '\'' +
                ", user=" + user +
                '}';
    }
}
