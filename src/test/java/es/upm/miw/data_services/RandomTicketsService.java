package es.upm.miw.data_services;

import es.upm.miw.documents.Ticket;
import es.upm.miw.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RandomTicketsService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Ticket> randomTickets(LocalDateTime fromDate, int numberOfTickets) {
        Map<String, Integer> mapArticleAmount = new HashMap<>();
        mapArticleAmount.put("8400000000017", 2);
        mapArticleAmount.put("8400000000024", 1);
        return new RandomTicketsBuilder(articleRepository).randomTickets(fromDate, numberOfTickets, mapArticleAmount).build();
    }
}
