package es.upm.miw.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.documents.Shopping;
import es.upm.miw.documents.ShoppingState;
import es.upm.miw.documents.Article;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingModificationStateOrAmountDto {

    private String description;

    private Integer amount;

    private BigDecimal discount;

    private BigDecimal retailPrice;

    private BigDecimal totalPrice;

    private ShoppingState shoppingState;

    private Article article;

    public ShoppingModificationStateOrAmountDto() {
        // Empty for framework
    }

    public ShoppingModificationStateOrAmountDto(
            String description,
            Integer amount,
            BigDecimal discount,
            BigDecimal retailPrice,
            BigDecimal totalPrice,
            ShoppingState shoppingState,
            Article article) {

        this.description = description;
        this.amount = amount;
        this.discount = discount;
        this.retailPrice = retailPrice;
        this.totalPrice = totalPrice;
        this.shoppingState = shoppingState;
        this.article = article;
    }

    public ShoppingModificationStateOrAmountDto(Shopping shopping) {

        this.description = shopping.getDescription();
        this.amount = shopping.getAmount();
        this.discount = shopping.getDiscount();
        this.retailPrice = shopping.getRetailPrice();
        this.totalPrice = shopping.getShoppingTotal();
        this.shoppingState = shopping.getShoppingState();
        this.article = shopping.getArticle();
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

    public Article getArticle() { return article; }

    public Shopping transformModifiedShoppingToShopping() {
        Shopping shopping = new Shopping();
        shopping.setDescription(this.getDescription());
        shopping.setAmount(this.getAmount());
        shopping.setDiscount(this.getDiscount());
        shopping.setRetailPrice(this.getRetailPrice());
        shopping.setShoppingState(this.getShoppingState());
        shopping.setArticle(this.getArticle());
        return shopping;
    }

    @Override
    public String toString() {
        return "ShoppingModificationStateOrAmountDto [description=" + description + ", retailPrice=" + retailPrice + ", amount=" + amount
                + ", discount=" + discount + ", totalPrice=" + totalPrice + ", shoppingState=" + shoppingState + "]";
    }
}
