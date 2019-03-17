package es.upm.miw.dtos.input;

import es.upm.miw.dtos.validations.ListNotEmpty;

import javax.validation.constraints.NotNull;

public class FamilySizeInputDto {

    @NotNull
    private String reference;

    @NotNull
    private String description;

    @NotNull
    private String provider;

    @ListNotEmpty
    private String sizesArray;

    @Override
    public String toString() {
        return "FamilySizeInputDto{" +
                "reference='" + reference + '\'' +
                ", description='" + description + '\'' +
                ", provider='" + provider + '\'' +
                ", sizeArray='" + sizesArray + '\'' +
                '}';
    }
}
