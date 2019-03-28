package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.data_services.RandomTicketsBuilder;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Ticket;
import es.upm.miw.dtos.stock_prediction.PeriodicityType;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static es.upm.miw.business_controllers.StockPredictionController.CountArticleFromTicketsGroupByPeriodicityAlgorithm.countArticleFromTicketsGroupByPeriodicity;
import static es.upm.miw.business_controllers.StockPredictionController.GroupTicketsByPeriodicityAlgorithm.groupTicketsByPeriodicity;
import static org.junit.Assert.assertEquals;

@TestConfig
class StockPredictionControllerIT {

    TicketRepository mockTicketRepository;
    Article article8400000000017;
    Article article8400000000024;
    @Autowired
    private StockPredictionController controller;
    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    public void before() {
        article8400000000017 = articleRepository.findById("8400000000017").orElse(null);
        article8400000000024 = articleRepository.findById("8400000000024").orElse(null);
    }

    @Test
    void testGroupTicketsByPeriodicityAlgorithm() {
        Map<String, List<Ticket>> groupTicketsByPeriodicity = groupTicketsByPeriodicity(
                randomTickets(LocalDateTime.now().minusMonths(1), 514),
                PeriodicityType.WEEKLY);
        System.out.print("groupTicketsByPeriodicity..." + groupTicketsByPeriodicity);

        int countTickets = groupTicketsByPeriodicity.entrySet().stream().mapToInt(entry -> entry.getValue().size()).sum();
        assertEquals(514, countTickets);
    }

    @Test
    void testCountArticleFromTicketsGroupByPeriodicityAlgorithm() {
        Map<String, Integer> countArticleFromTicketsGroupByPeriodicity = countArticleFromTicketsGroupByPeriodicity(
                article8400000000017,
                groupTicketsByPeriodicity(
                        randomTickets(LocalDateTime.now().minusMonths(12), 514),
                        PeriodicityType.MONTHLY));
        System.out.print("countArticleFromTicketsGroupByPeriodicity...\n" + countArticleFromTicketsGroupByPeriodicity);

        int countArticles = countArticleFromTicketsGroupByPeriodicity.entrySet().stream().mapToInt(entry -> entry.getValue()).sum();
        assertEquals(1028, countArticles);
    }

    private List<Ticket> randomTickets(LocalDateTime fromDate, int numberOfTickets) {
        return new RandomTicketsBuilder().
                fromDate(fromDate)
                .numberOfTickets(numberOfTickets)
                .addTicketArticle(article8400000000017, 2)
                .addTicketArticle(article8400000000024, 1)
                .build();
    }

    /*
    new StockPredictionOutputDto[]{
        new StockPredictionOutputDto(PeriodType.WEEK, 1, 1028),
                new StockPredictionOutputDto(PeriodType.WEEK, 2, 964),
                new StockPredictionOutputDto(PeriodType.WEEK, 3, 900),
                new StockPredictionOutputDto(PeriodType.WEEK, 4, 837)
    };
    */

}
