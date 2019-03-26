package es.upm.miw.data_services;

import es.upm.miw.documents.Article;
import es.upm.miw.documents.Shopping;
import es.upm.miw.documents.Ticket;
import es.upm.miw.documents.User;
import es.upm.miw.repositories.ArticleRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomTicketsBuilder {
    private Map<Article, Integer> articles = new HashMap<>();
    private LocalDateTime fromDate;
    private LocalDateTime toDate = LocalDateTime.now();
    private int numberOfTickets = 10;

    public static List<Ticket> randomTickets(ArticleRepository articleRepository) {
        return new RandomTicketsBuilder().
                fromDate(LocalDateTime.now().minusMonths(1))
                .numberOfTickets(5)
                .addTicketArticle(articleRepository.findById("8400000000017").orElse(null), 3)
                .addTicketArticle(articleRepository.findById("8400000000024").orElse(null), 2)
                .build();
    }

    private RandomTicketsBuilder numberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
        return this;
    }

    public RandomTicketsBuilder fromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public RandomTicketsBuilder toDate(LocalDateTime toDate) {
        this.toDate = toDate;
        return this;
    }

    public RandomTicketsBuilder addTicketArticle(Article article, int amount) {
        articles.put(article, amount);
        return this;
    }

    public List<Ticket> build() {
        List<Ticket> tickets = new ArrayList<>();
        long fromDateInSecs = fromDate.atZone(ZoneId.systemDefault()).toEpochSecond();
        long toDateInSecs = toDate.atZone(ZoneId.systemDefault()).toEpochSecond();
        for (int i = 0; i < numberOfTickets; i++) {
            LocalDateTime creationDate = Instant.ofEpochSecond(random(fromDateInSecs, toDateInSecs)).atZone(ZoneId.systemDefault()).toLocalDateTime();
            tickets.add(new TicketBuilder().idOfDay(i).creationDate(creationDate).articles(articles).build());
        }

        return tickets;
    }

    private long random(long min, long max) {
        return (long) (Math.random() * ((max - min) + 1)) + min;
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

        public TicketBuilder articles(Map<Article, Integer> articles) {
            articles.forEach((article, amount) -> addShopping(new Shopping(amount, BigDecimal.ZERO, article)));
            return this;
        }

    }
}
