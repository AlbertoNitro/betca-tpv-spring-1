package es.upm.miw.business_controllers;

import es.upm.miw.business_controllers.stock_prediction.StockPredictionAlgorithm;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Ticket;
import es.upm.miw.dtos.input.PeriodicityType;
import es.upm.miw.dtos.input.StockPredictionInputDto;
import es.upm.miw.dtos.output.StockPredictionOutputDto;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

import static es.upm.miw.business_controllers.stock_prediction.CountArticleFromTicketsGroupByPeriodicityFunction.countArticleFromTicketsGroupByPeriodicity;
import static es.upm.miw.business_controllers.stock_prediction.GroupTicketsByPeriodicityFunction.groupTicketsByPeriodicity;
import static es.upm.miw.business_controllers.stock_prediction.MapToPointArrayConverter.convertToPointArray;


@Controller
public class StockPredictionController {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public StockPredictionOutputDto[] calculateStockPrediction(StockPredictionInputDto input) {
        String articleCode = input.getArticleCode();
        Article article = articleRepository.findById(articleCode)
                .orElseThrow(() -> new NotFoundException("Article code (" + articleCode + ")"));
        List<Ticket> ticketsByArticle = ticketRepository.findByShoppingListArticle(articleCode);
        if (!ticketsByArticle.isEmpty()) {
            PeriodicityType periodicityType = input.getPeriodicityType();
            double[][] observationData = convertToPointArray(
                    countArticleFromTicketsGroupByPeriodicity(
                            article,
                            groupTicketsByPeriodicity(ticketsByArticle, periodicityType)));
            final int periodsNumber = input.getPeriodsNumber();
            final int observationDataPeriods = observationData.length;
            StockPredictionAlgorithm stockPredictionAlgorithm = new StockPredictionAlgorithm(article.getStock(), observationData);
            StockPredictionOutputDto[] output = new StockPredictionOutputDto[periodsNumber];
            for (int i = 0; i < output.length; i++) {
                int periodNumber = i + 1;
                output[i] = new StockPredictionOutputDto(
                        periodicityType.periodType(),
                        periodNumber,
                        stockPredictionAlgorithm.predict(observationDataPeriods + periodNumber));
            }
            return output;
        }
        return new StockPredictionOutputDto[0];
    }

    public TicketRepository getTicketRepository() {
        return ticketRepository;
    }

    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

}
