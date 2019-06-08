package es.upm.miw.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.validation.constraints.NotNull;

//@JsonInclude(Include.NON_NULL)
public class OrderArticleDto {

    @NotNull
    private String code;

    private String description;

    private double retailPrice;

    private Integer amount;

    private double discount;

    private double total;

    private Boolean committed;

    private String provider;

    public OrderArticleDto() {
        // Empty for framework
    }

    public OrderArticleDto(String code, String description, double retailPrice, int amount, double discount, double total,
                           Boolean committed, String provider) {
        this.code = code;
        this.description = description;
        this.retailPrice = retailPrice;
        this.amount = amount;
        this.discount = discount;
        this.total = total;
        this.committed = committed;
        this.provider = provider;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isCommitted() {
        return committed;
    }

    public void setCommitted(boolean committed) {
        this.committed = committed;
    }

    public String getProvider() { return provider; }

    public void setProvider(String provider) { this.provider = provider; }

    @Override
    public String toString() {
        return "OrderArticleDto{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", retailPrice=" + retailPrice +
                ", amount=" + amount +
                ", discount=" + discount +
                ", total=" + total +
                ", committed=" + committed +
                ", provider='" + provider + '\'' +
                '}';
    }
}
