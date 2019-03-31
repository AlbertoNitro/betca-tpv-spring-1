package es.upm.miw.business_controllers.stock_prediction;

import es.upm.miw.TestConfig;
import es.upm.miw.data_services.RandomTicketsService;
import es.upm.miw.documents.Ticket;
import es.upm.miw.dtos.input.PeriodicityType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static es.upm.miw.business_controllers.stock_prediction.GroupTicketsByPeriodicityFunction.groupTicketsByPeriodicity;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@TestConfig
class GroupTicketsByPeriodicityFunctionIT {

    @Autowired
    private RandomTicketsService randomTicketsService;

    @Test
    void groupTicketsByPeriodicityWeeklyShouldReturn514Tickets() {
        Map<String, List<Ticket>> groupTicketsByPeriodicity = groupTicketsByPeriodicity(
                randomTicketsService.randomTickets(LocalDateTime.now().minusMonths(1), 514),
                PeriodicityType.WEEKLY);
        System.out.print("groupTicketsByPeriodicity..." + groupTicketsByPeriodicity);

        int countTickets = groupTicketsByPeriodicity.entrySet().stream().mapToInt(entry -> entry.getValue().size()).sum();
        assertThat(countTickets, is(514));
    }

}
