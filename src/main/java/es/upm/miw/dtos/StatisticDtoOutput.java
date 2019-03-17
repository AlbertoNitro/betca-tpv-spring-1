package es.upm.miw.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

public class StatisticDtoOutput {

    @NotNull
    private String name;

    @NotNull
    private Double value;

    public StatisticDtoOutput() {
        // empty  framework
    }

    public StatisticDtoOutput(@NotNull String name, @NotNull Double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public List<StatisticDtoOutput> createStatisticsDto(Map<String, Double> ticketList) {
        List<StatisticDtoOutput> listStatisticsDTO = new ArrayList<>();

        ticketList.forEach((date, value) -> listStatisticsDTO.add(new StatisticDtoOutput(date, value)));

        return listStatisticsDTO;
    }

    @Override
    public String toString() {
        return "{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
