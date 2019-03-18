package es.upm.miw.dtos;

import es.upm.miw.documents.Article;

import java.math.BigDecimal;

public class ArticleSearchDto {

    private String code;

    private String description;

    private Integer stock;

    private BigDecimal retailPrice;

    public ArticleSearchDto() {
    }

    public ArticleSearchDto(String code, String description, Integer stock, BigDecimal retailPrice) {
        this.code = code;
        this.description = description;
        this.retailPrice = retailPrice;
        this.stock = stock;
    }

    public ArticleSearchDto(Article article) {
        this.code = article.getCode();
        this.description = article.getDescription();
        this.retailPrice = article.getRetailPrice();
        this.stock = article.getStock();
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    @Override
    public String toString() {
        return "ArticleSearchDto{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                ", retailPrice=" + retailPrice +
                '}';
    }
}
