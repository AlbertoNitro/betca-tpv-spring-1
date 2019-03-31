package es.upm.miw.business_controllers.stock_prediction;

import es.upm.miw.TestConfig;
import es.upm.miw.data_services.RandomTicketsService;
import es.upm.miw.documents.Article;
import es.upm.miw.dtos.stock_prediction.PeriodicityType;
import es.upm.miw.repositories.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Map;

import static es.upm.miw.business_controllers.stock_prediction.CountArticleFromTicketsGroupByPeriodicityFunction.countArticleFromTicketsGroupByPeriodicity;
import static es.upm.miw.business_controllers.stock_prediction.GroupTicketsByPeriodicityFunction.groupTicketsByPeriodicity;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@TestConfig
class CountArticleFromTicketsGroupByPeriodicityFunctionIT {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private RandomTicketsService randomTicketsService;

    @Test
    void countArticleFromTicketsGroupByPeriodicityMonthlyShouldReturn1028Articles() {
        Article article8400000000017 = articleRepository.findByCode("8400000000017");
        Map<String, Integer> countArticleFromTicketsGroupByPeriodicity = countArticleFromTicketsGroupByPeriodicity(
                article8400000000017,
                groupTicketsByPeriodicity(
                        randomTicketsService.randomTickets(LocalDateTime.now().minusMonths(12), 514),
                        PeriodicityType.MONTHLY));
        System.out.print("countArticleFromTicketsGroupByPeriodicity...\n" + countArticleFromTicketsGroupByPeriodicity);

        int countArticles = countArticleFromTicketsGroupByPeriodicity.entrySet().stream().mapToInt(entry -> entry.getValue()).sum();
        assertThat(countArticles, is(1028));
    }


}
