package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.data_services.RandomTicketsService;
import es.upm.miw.dtos.stock_prediction.PeriodicityType;
import es.upm.miw.dtos.stock_prediction.StockPredictionInputDto;
import es.upm.miw.dtos.stock_prediction.StockPredictionOutputDto;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestConfig
class StockPredictionControllerIT {
    @Autowired
    private StockPredictionController controller;
    @Autowired
    private RandomTicketsService randomTicketsService;

    @Test
    void calculateStockPredictionWithNotExistArticleShouldThrowException() {
        assertThrows(NotFoundException.class, () -> controller.calculateStockPrediction(
                new StockPredictionInputDto("notExistArticle", PeriodicityType.WEEKLY, 3)));
    }

    @Test
    void calculateStockPredictionWith3PeriodsShouldReturn3StockPredictionOutputDto() {
        TicketRepository mockTicketRepository = mock(TicketRepository.class);
        when(mockTicketRepository.findByShoppingListArticle("8400000000017"))
                .thenReturn(randomTicketsService.randomTickets(LocalDateTime.now().minusWeeks(12), 514));
        controller.setTicketRepository(mockTicketRepository);

        StockPredictionOutputDto[] stockPredictionOutputDtos = controller.calculateStockPrediction(
                new StockPredictionInputDto("8400000000017", PeriodicityType.WEEKLY, 3));
        assertThat(stockPredictionOutputDtos.length, is(3));
        verify(mockTicketRepository).findByShoppingListArticle("8400000000017");
    }

}
