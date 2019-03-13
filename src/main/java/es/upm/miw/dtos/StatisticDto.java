package es.upm.miw.dtos;

import javax.validation.constraints.NotNull;

public class StatisticDto {

    @NotNull
    private String name;

    @NotNull
    private String value;

    public StatisticDto() {
        // empty  framework
    }

    public StatisticDto(@NotNull String name, @NotNull String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
