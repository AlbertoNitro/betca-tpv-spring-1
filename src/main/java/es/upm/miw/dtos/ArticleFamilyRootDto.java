package es.upm.miw.dtos;

import es.upm.miw.documents.ArticlesFamily;
import es.upm.miw.documents.FamilyType;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class ArticleFamilyRootDto {

    @NotNull
    private String description;
    @NotNull
    private String reference;
    @NotNull
    private String code;
    @NotNull
    private BigDecimal retailPrice;
    @NotNull
    private FamilyType familyType;
    @NotNull
    private List<ArticlesFamily> articlesFamilyList;

    public ArticleFamilyRootDto(){}


    public ArticleFamilyRootDto(String code, String description, BigDecimal retailPrice) {
        this.description = description;
        this.code = code;
        this.retailPrice = retailPrice;
    }

    public ArticleFamilyRootDto(String description, List<ArticlesFamily> articlesFamilyList ) {
        this.familyType = familyType;
        this.description = description;
        this.articlesFamilyList = articlesFamilyList;
    }

    public ArticleFamilyRootDto(String reference, String description) {
        this.description = description;
        this.reference = reference;
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

    @Override
    public String toString() {
        return "ArticleFamilyRootDto{" +
                "description='" + description + '\'' +
                ", reference='" + reference + '\'' +
                ", code='" + code + '\'' +
                ", retailPrice='" + retailPrice + '\'' +
                ", familyType=" + familyType +
                ", articlesFamilyList=" + articlesFamilyList +
                '}';
    }
}
