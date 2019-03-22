package es.upm.miw.business_controllers;

import es.upm.miw.dtos.stock_prediction.PeriodType;
import es.upm.miw.dtos.stock_prediction.StockPredictionInputDto;
import es.upm.miw.dtos.stock_prediction.StockPredictionOutputDto;
import es.upm.miw.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class StockPredictionController {
    @Autowired
    private TicketRepository ticketRepository;

    public StockPredictionOutputDto[] calculateStockPrediction(StockPredictionInputDto input) {
        return new StockPredictionOutputDto[]{
                new StockPredictionOutputDto(PeriodType.WEEK, 1, 1028),
                new StockPredictionOutputDto(PeriodType.WEEK, 2, 964),
                new StockPredictionOutputDto(PeriodType.WEEK, 3, 900),
                new StockPredictionOutputDto(PeriodType.WEEK, 4, 837)
        };
    }

}
