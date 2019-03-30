package es.upm.miw.dtos;

import es.upm.miw.documents.ArticlesFamily;
import es.upm.miw.documents.FamilyType;

import java.math.BigDecimal;
import java.util.List;

public class ArticleFamilyRootDto {

    private String description;

    private String reference;

    private Integer stock;

    private String size;

    private String code;

    private BigDecimal retailPrice;

    private FamilyType familyType;

    private List<ArticlesFamily> articlesFamilyList;

    public ArticleFamilyRootDto() {
    }


    public ArticleFamilyRootDto(FamilyType familyType, String code, String description, BigDecimal retailPrice) {
        this.familyType = familyType;
        this.description = description;
        this.code = code;
        this.retailPrice = retailPrice;
    }

    public ArticleFamilyRootDto(FamilyType familyType, String description, List<ArticlesFamily> articlesFamilyList) {
        this.familyType = familyType;
        this.description = description;
        this.articlesFamilyList = articlesFamilyList;
    }

    public ArticleFamilyRootDto(FamilyType familyType, String reference, String description) {
        this.familyType = familyType;
        this.description = description;
        this.reference = reference;
    }

    public ArticleFamilyRootDto(String size, Integer stock, String code){
        this.code = code;
        this.size = size;
        this.stock = stock;
    }

    public FamilyType getFamilyType() {
        return familyType;
    }

    public void setFamilyType(FamilyType familyType) {
        this.familyType = familyType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ArticlesFamily> getArticlesFamilyList() {
        return articlesFamilyList;
    }

    public void setArticlesFamilyList(List<ArticlesFamily> articlesFamilyList) {
        this.articlesFamilyList = articlesFamilyList;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ArticleFamilyRootDto{" +
                "description='" + description + '\'' +
                ", reference='" + reference + '\'' +
                ", stock=" + stock +
                ", size='" + size + '\'' +
                ", code='" + code + '\'' +
                ", retailPrice=" + retailPrice +
                ", familyType=" + familyType +
                ", articlesFamilyList=" + articlesFamilyList +
                '}';
    }
}
