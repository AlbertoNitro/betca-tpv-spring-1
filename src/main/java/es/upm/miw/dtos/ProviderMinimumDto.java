package es.upm.miw.dtos;

import javax.validation.constraints.NotNull;

public class ProviderMinimumDto {

    private String id;

    @NotNull
    private String company;

    public ProviderMinimumDto() {
    }

    public ProviderMinimumDto(String id, String company) {
        this.id = id;
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "ProviderMinimumDto{" +
                "id='" + id + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
