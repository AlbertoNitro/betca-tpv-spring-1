package es.upm.miw.business_controllers.stock_prediction;

import es.upm.miw.documents.Ticket;
import es.upm.miw.dtos.input.PeriodicityType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class GroupTicketsByPeriodicityFunction {

    private GroupTicketsByPeriodicityFunction() {
        super();
    }

    public static Map<String, List<Ticket>> groupTicketsByPeriodicity(List<Ticket> tickets, PeriodicityType periodicityType) {
        Map<String, List<Ticket>> map = tickets.stream().collect(groupingBy(t -> t.getCreationDate().format(periodicityType.dateTimeFormatter())));
        List<String> sortedKeys = map.keySet().stream().sorted().collect(Collectors.toList());
        LinkedHashMap<String, List<Ticket>> sorted = new LinkedHashMap<>();
        sortedKeys.forEach(k -> sorted.put(k, map.get(k)));
        return sorted;
    }
}