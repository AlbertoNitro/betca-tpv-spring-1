package es.upm.miw.business_controllers;

import es.upm.miw.documents.Article;
import es.upm.miw.documents.Shopping;
import es.upm.miw.documents.Ticket;
import es.upm.miw.dtos.stock_prediction.PeriodicityType;
import es.upm.miw.dtos.stock_prediction.StockPredictionInputDto;
import es.upm.miw.dtos.stock_prediction.StockPredictionOutputDto;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static es.upm.miw.business_controllers.StockPredictionController.CountArticleFromTicketsGroupByPeriodicityAlgorithm.countArticleFromTicketsGroupByPeriodicity;
import static es.upm.miw.business_controllers.StockPredictionController.GroupTicketsByPeriodicityAlgorithm.groupTicketsByPeriodicity;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.groupingBy;


@Controller
public class StockPredictionController {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public StockPredictionOutputDto[] calculateStockPrediction(StockPredictionInputDto input) {
        String articleCode = input.getArticleCode();
        Article article = this.articleRepository.findById(articleCode)
                .orElseThrow(() -> new NotFoundException("Article code (" + articleCode + ")"));

        List<Ticket> ticketsByArticle = ticketRepository.findByShoppingListArticle(articleCode);
        if (!ticketsByArticle.isEmpty()) {
            PeriodicityType periodicityType = input.getPeriodicityType();
            Map<String, Integer> articleCountGroupByPeriodMap = countArticleFromTicketsGroupByPeriodicity(
                    article,
                    groupTicketsByPeriodicity(ticketsByArticle, periodicityType));
        }

        //TODO: Prediction Algoritm
        return null;

    }

    public TicketRepository getTicketRepository() {
        return ticketRepository;
    }

    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    static class CountArticleFromTicketsGroupByPeriodicityAlgorithm {
        static Map<String, Integer> countArticleFromTicketsGroupByPeriodicity(Article article, Map<String, List<Ticket>> ticketsGroupByPeriodicityMap) {
            final Map<String, Integer> articleCountGroupByPeriodMap = new LinkedHashMap<>();
            ticketsGroupByPeriodicityMap.forEach((periodicity, tickets) -> {
                int articleCount = tickets.stream()
                        .map(t -> stream(t.getShoppingList()).filter(shopping -> shopping.getArticle().equals(article)).findFirst().orElse(null))
                        .map(Shopping::getAmount).mapToInt(Integer::intValue).sum();
                articleCountGroupByPeriodMap.put(periodicity, articleCount);
            });
            return articleCountGroupByPeriodMap;
        }
    }

    static class GroupTicketsByPeriodicityAlgorithm {
        static Map<String, List<Ticket>> groupTicketsByPeriodicity(List<Ticket> tickets, PeriodicityType periodicityType) {
            Map<String, List<Ticket>> map = tickets.stream().collect(groupingBy(t -> t.getCreationDate().format(periodicityType.dateTimeFormatter())));
            List<String> sortedKeys = map.keySet().stream().sorted().collect(Collectors.toList());
            LinkedHashMap<String, List<Ticket>> sorted = new LinkedHashMap<>();
            sortedKeys.forEach(k -> sorted.put(k, map.get(k)));
            return sorted;
        }
    }
}
