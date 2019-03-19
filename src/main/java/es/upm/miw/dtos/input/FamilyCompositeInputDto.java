package es.upm.miw.dtos.input;

import es.upm.miw.documents.FamilyType;

import javax.validation.constraints.NotNull;

public class FamilyCompositeInputDto {

    @NotNull
    private FamilyType familyType;

    private String reference;

    @NotNull
    private String description;

    public FamilyCompositeInputDto() {
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
        return "FamilyCompositeInputDto{" +
                "familyType=" + familyType +
                ", reference='" + reference + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
