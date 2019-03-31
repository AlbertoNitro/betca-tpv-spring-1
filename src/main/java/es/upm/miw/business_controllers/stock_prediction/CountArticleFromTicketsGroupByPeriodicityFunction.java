package es.upm.miw.business_controllers.stock_prediction;

import es.upm.miw.documents.Article;
import es.upm.miw.documents.Shopping;
import es.upm.miw.documents.Ticket;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;

public class CountArticleFromTicketsGroupByPeriodicityFunction {
    public static Map<String, Integer> countArticleFromTicketsGroupByPeriodicity(Article article, Map<String, List<Ticket>> ticketsGroupByPeriodicityMap) {
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