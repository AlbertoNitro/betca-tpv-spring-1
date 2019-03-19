package es.upm.miw.business_controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.upm.miw.documents.Ticket;
import es.upm.miw.dtos.output.StatisticDtoOutput;
import es.upm.miw.repositories.TicketRepository;

@Controller
public class StatisticController {
    @Autowired
    private TicketRepository ticketRepository;

    private LocalDateTime convertStringToDate(String date) {
        return LocalDateTime.parse(date);
    }

    public List<StatisticDtoOutput> getDataStatisticAverageDailyExpense(String dateFrom, String dateTo) {
        List<Ticket> tickets = this.ticketRepository.findByCreationDateBetween(this.convertStringToDate(dateFrom), this.convertStringToDate(dateTo));

        Map<String, Double> listCollectors = tickets.stream().collect(
                Collectors.groupingBy(ticket -> ticket.getCreationDate().toLocalDate().toString(), Collectors.averagingDouble(ticket -> ticket.getTotal().doubleValue())));

        return new StatisticDtoOutput().createStatisticsDto(listCollectors);
    }

    public List<StatisticDtoOutput> getDataStatisticTotalSalesPerDay(String dateFrom, String dateTo) {
        List<Ticket> tickets = this.ticketRepository.findByCreationDateBetween(this.convertStringToDate(dateFrom), this.convertStringToDate(dateTo));

        Map<String, Double> listCollectors = tickets.stream().collect(
                Collectors.groupingBy(ticket -> ticket.getCreationDate().toLocalDate().toString(), Collectors.summingDouble(ticket -> ticket.getTotal().doubleValue())));

        return new StatisticDtoOutput().createStatisticsDto(listCollectors);
    }
}
