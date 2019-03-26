package es.upm.miw.data_services;

import es.upm.miw.documents.Article;
import es.upm.miw.documents.Shopping;
import es.upm.miw.documents.Ticket;
import es.upm.miw.documents.User;
import es.upm.miw.repositories.ArticleRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTicketsBuilder {
    private List<Article> articles = new ArrayList<>();
    private LocalDateTime fromDate;
    private LocalDateTime toDate = LocalDateTime.now();

    public static List<Ticket> randomTickets(ArticleRepository articleRepository) {
        return new RandomTicketsBuilder().
                fromDate(LocalDateTime.now().minusMonths(3))
                .addArticle(articleRepository.findById("8400000000017").orElse(null))
                .addArticle(articleRepository.findById("8400000000024").orElse(null))
                .build();
    }

    public RandomTicketsBuilder fromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public RandomTicketsBuilder toDate(LocalDateTime toDate) {
        this.toDate = toDate;
        return this;
    }

    public RandomTicketsBuilder addArticle(Article article) {
        articles.add(article);
        return this;
    }

    public List<Ticket> build() {
        List<Ticket> tickets = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.from(fromDate);
        while (startDate.isBefore(toDate)) {
            int randomTicketsPerDay = random(1, 3);
            for (int i = 0; i < randomTicketsPerDay; i++) {
                tickets.add(new TicketBuilder().idOfDay(i).creationDate(startDate).articles(articles).build());
            }
            startDate = startDate.plusDays(random(1, 3));
        }
        return tickets;
    }

    private int random(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    private class TicketBuilder {
        private int idOfDay;
        private LocalDateTime creationDate;
        private String reference;
        private List<Shopping> shoppingList = new ArrayList<>();
        private BigDecimal card = BigDecimal.ZERO;
        private BigDecimal cash = BigDecimal.ZERO;
        private BigDecimal voucher = BigDecimal.ZERO;
        private String note;
        private User user;

        public Ticket build() {
            shoppingList.forEach(s -> cash = cash.add(s.getShoppingTotal()));
            Ticket ticket = new Ticket(idOfDay, card, cash, voucher, shoppingList.toArray(new Shopping[shoppingList.size()]), user);
            ticket.setId(creationDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + idOfDay);
            ticket.setCreationDate(creationDate);
            return ticket;
        }

        public TicketBuilder idOfDay(int id) {
            this.idOfDay = id;
            return this;
        }

        public TicketBuilder creationDate(LocalDateTime creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public TicketBuilder addShopping(Shopping shopping) {
            shoppingList.add(shopping);
            return this;
        }

        public TicketBuilder articles(List<Article> articles) {
            articles.forEach(a -> addShopping(new Shopping(random(1, 10), BigDecimal.ZERO, a)));
            return this;
        }

    }
}
