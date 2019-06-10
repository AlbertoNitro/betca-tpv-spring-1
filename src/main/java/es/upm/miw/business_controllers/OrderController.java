package es.upm.miw.business_controllers;

import es.upm.miw.business_services.EmailServiceImpl;
import es.upm.miw.documents.*;
import es.upm.miw.dtos.OrderArticleDto;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Order;
import es.upm.miw.documents.OrderLine;
import es.upm.miw.documents.Provider;
import es.upm.miw.dtos.OrderDto;
import es.upm.miw.dtos.OrderSearchDto;
import es.upm.miw.repositories.OrderRepository;
import es.upm.miw.repositories.TicketRepository;
import org.apache.logging.log4j.LogManager;
import es.upm.miw.repositories.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    TicketRepository ticketRepository;

    TicketController ticketController;

    @Autowired
    EmailServiceImpl emailService;

    private List<OrderSearchDto> orderSearchDtos;

    @Autowired
    private ProviderRepository providerRepository;

    public static String SEARCHWORD = "";

    public Order closeOrder(String orderId, OrderLine[] orderLine) {
        Order closeOrder = orderRepository.findById(orderId).orElse(null);
        if (orderLine.length > 0 && closeOrder != null) {
            closeOrder.close();
            closeOrder.setOrderLines(orderLine);
            updateArticleStock(closeOrder);
            closeOrder = orderRepository.save(closeOrder);
        } else {
            throw new BadRequestException("orderLine is empty");
        }

        return closeOrder;
    }

    public List<User> sendArticlesFromOrderLine(OrderLine[] orderLine) {
        List<User> users = new ArrayList<>();
        for (OrderLine orderLineSingle : orderLine) {
            LogManager.getLogger().debug("Probando");
            users = getUsersWithNotCommittedTickets(orderLineSingle.getArticle().getCode());
            for (User user : users) {
                LogManager.getLogger().debug("Usuarios: " + user.getEmail());
            }
        }
        return users;
    }

    private void updateArticleStock(@NotNull Order order) {

        OrderLine[] orderLine = order.getOrderLines();
        List<User> users;

        for (OrderLine orderLineSingle : orderLine) {
            Article article = orderLineSingle.getArticle();
            Article articleDB = this.articleRepository.findById(article.getCode()).orElse(null);
            users = getUsersWithNotCommittedTickets(article.getCode());
            if(articleDB != null) {
                articleDB.setStock(articleDB.getStock() + orderLineSingle.getFinalAmount());
            } else {
                return;
            }
            articleRepository.save(articleDB);
            for (User user : users) {
                sendNotificationAvailableStock(user, "Stock available of " + articleDB.getReference());
            }
        }
    }

    private void sendNotificationAvailableStock(User user, String message) {
        emailService.sendSimpleMessage(user.getEmail(), "Notification", message);
    }

    public OrderDto create(String descriptionOrder, String providerId, String[] idArticles, Integer[] requiredAmount) {
        OrderLine[] orderLines = new OrderLine[idArticles.length];
        for (int i = 0; i < idArticles.length; i++) {
            if(this.articleRepository.findById(idArticles[i]).isPresent()) {
                Article article = this.articleRepository.findById(idArticles[i]).get();
                orderLines[i] = new OrderLine(article, requiredAmount[i]);
            }
        }
        if((providerRepository.findById(providerId).isPresent())){
            Provider provider = providerRepository.findById(providerId).get();
            Order order = new Order(descriptionOrder, provider, orderLines);
            this.orderRepository.save(order);
            return new OrderDto(order);
        }
        return null;
    }

    public List<OrderSearchDto> findByDescription(String id) {
        OrderSearchDto orderSearchDto = null;
        orderSearchDtos = new ArrayList<>();
        Optional<Order> order = orderRepository.findByDescription(id);
        if(order.isPresent()){
            for (OrderLine orderLine : order.get().getOrderLines()) {
                orderSearchDto = new OrderSearchDto(order.get().getDescription(), orderLine.getArticle().getDescription(), orderLine.getRequiredAmount(), orderLine.getFinalAmount(), order.get().getOpeningDate(), order.get().getClosingDate());
                orderSearchDtos.add(orderSearchDto);
            }
        }
        return orderSearchDtos;
    }

    public List<OrderSearchDto> readAll() {
        orderSearchDtos = new ArrayList<>();
        for (OrderDto dto : orderRepository.findAllOrdersByOpeningDateDesc()) {
            for (OrderLine orderLine : dto.getOrderLines()) {
                createAddOrderSearchDto(dto);
            }
        }
        return orderSearchDtos;
    }

    public List<OrderSearchDto> searchOrder(String orderDescription, String articleDescription, Boolean onlyClosingDate) {
        SEARCHWORD = validateValue(orderDescription, articleDescription);
        orderSearchDtos = new ArrayList<>();
        for (OrderDto dto : orderRepository.findAllOrdersByOpeningDateDesc()){
                String orderDescry = dto.getDescription().toLowerCase();
                if (orderDescription.isEmpty()) {
                    validateClosingDate(dto, onlyClosingDate);
                } else if (SEARCHWORD.contains(orderDescry)) {
                    validateClosingDate(dto, onlyClosingDate);
                }
        }
        return orderSearchDtos;
    }

    public void validateClosingDate(OrderDto dto, Boolean onlyClosingDate) {
        if (onlyClosingDate == false)
            createAddOrderSearchDto(dto);
        else if (dto.getClosingDate() != null)
            createAddOrderSearchDto(dto);
    }

    public void createAddOrderSearchDto(OrderDto dto) {
        OrderSearchDto orderSearchDto;
        orderSearchDto = new OrderSearchDto(dto.getId(), dto.getDescription(), "",0, 0, dto.getOpeningDate(), dto.getClosingDate());
        orderSearchDtos.add(orderSearchDto);
    }

    private List<User> getUsersWithNotCommittedTickets(String code) {
        List<Ticket> tickets = this.ticketRepository.findByShoppingListArticle(code);
        List<User> user = new ArrayList<>();
        for (Ticket item : tickets) {
            for (Shopping article : item.getShoppingList()) {
                if (article.getShoppingState() == ShoppingState.NOT_COMMITTED) {
                    user.add(item.getUser());
                }
            }
        }
        return user;
    }

    public List<OrderArticleDto> findById(String id) {
        List<OrderArticleDto> orderArticleDtos;
        OrderArticleDto orderArticleDto = null;
        orderArticleDtos = new ArrayList<>();
        Optional<Order> order = orderRepository.findById(id);
        for (OrderLine orderLine : order.get().getOrderLines()) {
            orderArticleDto = new OrderArticleDto(
                    orderLine.getArticle().getCode(),
                    orderLine.getArticle().getDescription(),
                    orderLine.getArticle().getRetailPrice().doubleValue(),
                    orderLine.getRequiredAmount(),
                    0.0,
                    0.0,
                    false,
                    orderLine.getArticle().getProvider().getId());
            orderArticleDtos.add(orderArticleDto);
        }
        return orderArticleDtos;
    }

    public void delete(String id) {
        Optional<Order> order = this.orderRepository.findById(id);
        if (order.isPresent()) {
            this.orderRepository.delete(order.get());
        }
    }

    public String validateValue(String value1, String value2){
        return (value1).trim().toLowerCase().toString() + " " + (value2).trim().toLowerCase().toString();
    }
}
