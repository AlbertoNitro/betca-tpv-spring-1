package es.upm.miw.dtos;

import es.upm.miw.documents.FamilyComposite;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class FamilySizeInputDto {

    @NotNull
    private String reference;

    @NotNull
    private String description;

    @NotNull
    private String provider;

    @NotNull
    private String sizeType;

    @NotNull
    private String smallestSize;

    @NotNull
    private String largestSize;

    @NotNull
    @Min(1)
    @Max(10)
    private String step;

    public FamilySizeInputDto(FamilyComposite familyComposite) {
    }

    public String getReference() {
        return reference;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "FamilySizeInputDto{" +
                "reference='" + reference + '\'' +
                ", description='" + description + '\'' +
                ", provider='" + provider + '\'' +
                ", sizeType='" + sizeType + '\'' +
                ", smallestSize='" + smallestSize + '\'' +
                ", largestSize='" + largestSize + '\'' +
                ", step='" + step + '\'' +
                '}';
    }
}
