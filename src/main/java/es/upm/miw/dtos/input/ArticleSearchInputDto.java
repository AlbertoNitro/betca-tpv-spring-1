package es.upm.miw.dtos.input;

import java.math.BigDecimal;

public class ArticleSearchInputDto {

    private String description;

    private Integer stock;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    public ArticleSearchInputDto() {
    }

    public ArticleSearchInputDto(String description, Integer stock, BigDecimal minPrice, BigDecimal maxPrice) {
        this.description = description;
        this.stock = stock;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Override
    public String toString() {
        return "ArticleSearchInputDto{" +
                "description='" + description + '\'' +
                ", stock=" + stock +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                '}';
    }
}
