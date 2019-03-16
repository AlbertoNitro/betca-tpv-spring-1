package es.upm.miw.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import es.upm.miw.documents.FamilyType;

public class ArticleFamilyMinimumDto {

    @JsonInclude(Include.NON_NULL)
    private FamilyType familyType;

    private String description;

    public ArticleFamilyMinimumDto() {
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

    @Override
    public String toString() {
        return "ArticleFamilyMinimumDto{" +
                "familyType=" + familyType +
                ", description='" + description + '\'' +
                '}';
    }
}
