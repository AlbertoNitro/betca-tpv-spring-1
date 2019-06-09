package es.upm.miw.dtos;

import es.upm.miw.documents.ArticlesFamily;
import es.upm.miw.documents.FamilyType;

import javax.validation.constraints.NotNull;

public class ArticleFamilyDto {

    @NotNull
    private FamilyType familyType;

    private String reference;

    @NotNull
    private String description;

    public ArticleFamilyDto() {
    }

    public ArticleFamilyDto(@NotNull FamilyType familyType, String reference, @NotNull String description) {
        this.familyType = familyType;
        this.reference = reference;
        this.description = description;
    }

    public ArticleFamilyDto(ArticlesFamily articlesFamily) {
        this.familyType = articlesFamily.getFamilyType();
        this.reference = articlesFamily.getReference();
        this.description = articlesFamily.getDescription();
    }

    public FamilyType getFamilyType() {
        return familyType;
    }

    public void setFamilyType(FamilyType familyType) {
        this.familyType = familyType;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ArticleFamilyDto{" +
                "familyType=" + familyType +
                ", reference='" + reference + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
