package es.upm.miw.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.documents.Shopping;
import es.upm.miw.documents.ShoppingState;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingModificationStateOrAmountDto {

    private String description;

    private Integer amount;

    private BigDecimal discount;

    private BigDecimal retailPrice;

    private BigDecimal totalPrice;

    private ShoppingState shoppingState;

    public ShoppingModificationStateOrAmountDto() {
        //Empty for framework
    }

    public ShoppingModificationStateOrAmountDto(Shopping shopping) {

        this.description = shopping.getDescription();
        this.amount = shopping.getAmount();
        this.discount = shopping.getDiscount();
        this.retailPrice = shopping.getRetailPrice();
        this.totalPrice = shopping.getShoppingTotal();
        this.shoppingState = shopping.getShoppingState();
    }

    public String getDescription() {
        return description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ShoppingState getShoppingState() {
        return shoppingState;
    }

    public void setShoppingState(ShoppingState shoppingState) {
        this.shoppingState = shoppingState;
    }

    public Shopping transformModifiedShoppingToShopping() {
        Shopping shopping = new Shopping();
        shopping.setDescription(this.getDescription());
        shopping.setAmount(this.getAmount());
        shopping.setDiscount(this.getDiscount());
        shopping.setRetailPrice(this.getRetailPrice());
        shopping.setShoppingState(this.getShoppingState());
        return shopping;
    }

    @Override
    public String toString() {
        return "ShoppingModificationStateOrAmountDto [description=" + description + ", retailPrice=" + retailPrice + ", amount=" + amount
                + ", discount=" + discount + ", totalPrice=" + totalPrice + ", shoppingState=" + shoppingState + "]";
    }
}
