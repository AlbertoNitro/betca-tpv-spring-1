package es.upm.miw.business_controllers;

import es.upm.miw.documents.Article;
import es.upm.miw.documents.Ticket;
import es.upm.miw.dtos.stock_prediction.PeriodType;
import es.upm.miw.dtos.stock_prediction.PeriodicityType;
import es.upm.miw.dtos.stock_prediction.StockPredictionInputDto;
import es.upm.miw.dtos.stock_prediction.StockPredictionOutputDto;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
            Map<String, List<Ticket>> ticketsGroupByPeriodicityMap =
                    ticketsByArticle.stream().collect(Collectors.groupingBy(t -> t.getCreationDate().format(periodicityType.dateTimeFormatter())));
        }

        return new StockPredictionOutputDto[]{
                new StockPredictionOutputDto(PeriodType.WEEK, 1, 1028),
                new StockPredictionOutputDto(PeriodType.WEEK, 2, 964),
                new StockPredictionOutputDto(PeriodType.WEEK, 3, 900),
                new StockPredictionOutputDto(PeriodType.WEEK, 4, 837)
        };
    }

    public TicketRepository getTicketRepository() {
        return ticketRepository;
    }

    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
}
