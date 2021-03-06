package es.upm.miw.dtos.input;

import es.upm.miw.dtos.ShoppingDto;
import es.upm.miw.dtos.validations.BigDecimalPositive;
import es.upm.miw.dtos.validations.ListNotEmpty;

import java.math.BigDecimal;
import java.util.List;

public class TicketCreationInputDto {

    private String userMobile;

    @BigDecimalPositive
    private BigDecimal cash;

    @BigDecimalPositive
    private BigDecimal card;

    @BigDecimalPositive
    private BigDecimal voucher;

    @ListNotEmpty
    private List<ShoppingDto> shoppingCart;

    private String note;

    private String giftNote;

    public TicketCreationInputDto() {
        // Empty for framework
    }

    public TicketCreationInputDto(String userMobile, BigDecimal cash, BigDecimal card, BigDecimal voucher, List<ShoppingDto> shoppingCart, String note, String giftNote) {
        this.userMobile = userMobile;
        this.cash = cash;
        this.card = card;
        this.voucher = voucher;
        this.shoppingCart = shoppingCart;
        this.note = note;
        this.giftNote = giftNote;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
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

    public List<ShoppingDto> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(List<ShoppingDto> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getGiftNote() {
        return giftNote;
    }

    public void setGiftNote(String giftNote) {
        this.giftNote = giftNote;
    }

    @Override
    public String toString() {
        return "TicketCreationInputDto{" +
                "userMobile='" + userMobile + '\'' +
                ", cash=" + cash +
                ", card=" + card +
                ", voucher=" + voucher +
                ", shoppingCart=" + shoppingCart +
                ", note='" + note + '\'' +
                ", giftNote='" + giftNote + '\'' +
                '}';
    }
}
