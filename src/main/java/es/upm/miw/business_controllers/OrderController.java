package es.upm.miw.business_controllers;

import es.upm.miw.business_services.EmailServiceImpl;
import es.upm.miw.documents.*;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.OrderRepository;
import es.upm.miw.repositories.TicketRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import es.upm.miw.dtos.OrderDto;
import es.upm.miw.dtos.OrderSearchDto;
import org.springframework.stereotype.Controller;


import javax.validation.constraints.NotNull;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    TicketRepository ticketRepository;

    TicketController ticketController;

    public Order closeOrder(String orderId, OrderLine[] orderLine) {
        Order closeOrder = orderRepository.findById(orderId).orElse(null);
        if(orderLine.length > 0) {
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
        for(OrderLine orderLineSingle : orderLine) {
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
        List<User> users = new ArrayList<>();

        for (OrderLine orderLineSingle : orderLine) {
            Article article = orderLineSingle.getArticle();
            Article articleDB = this.articleRepository.findById(article.getCode()).get();
            users = getUsersWithNotCommittedTickets(article.getCode());
            articleDB.setStock(articleDB.getStock() + orderLineSingle.getFinalAmount());
            articleRepository.save(articleDB);
            for (User user : users) {
                LogManager.getLogger().debug("Usuarios: " + user.getEmail());
                //EmailServiceImpl.sendSimpleMessage("miguelcalderon10@gmail.com", "Stock from article", "New stock from article");
            }
        }
    }

    private List<OrderSearchDto> orderSearchDtos;

    public static String SEARCHWORD = "";

    public List<OrderSearchDto> readAll() {
        orderSearchDtos = new ArrayList<>();
        for (OrderDto dto : orderRepository.findAllOrders()) {
            for (OrderLine orderLine : dto.getOrderLines()) {
                createAddOrderSearchDto(dto, orderLine);
            }
        }
        return orderSearchDtos;
    }

    public List<OrderSearchDto> searchOrder(String orderDescription, String articleDescription, Boolean onlyClosingDate) {
        SEARCHWORD = (orderDescription).trim().toLowerCase().toString() + " " + (articleDescription).trim().toLowerCase().toString();
        orderSearchDtos = new ArrayList<>();
        for (OrderDto dto : orderRepository.findAllOrders())
            for (OrderLine orderLine : dto.getOrderLines()) {
                String orderDescry = dto.getDescription().toLowerCase();
                String articleDescry = orderLine.getArticle().getDescription().toLowerCase();
                if (orderDescription == "" && articleDescription == "") {
                    validateClosingDate(dto, orderLine, onlyClosingDate);
                } else if (SEARCHWORD.contains(orderDescry) || SEARCHWORD.contains(articleDescry)) {
                    validateClosingDate(dto, orderLine, onlyClosingDate);
                }
            }
        return orderSearchDtos;
    }

    public void validateClosingDate(OrderDto dto, OrderLine orderLine, Boolean onlyClosingDate) {
        if (onlyClosingDate == false)
            createAddOrderSearchDto(dto, orderLine);
        else if (dto.getClosingDate() != null)
            createAddOrderSearchDto(dto, orderLine);
    }

    public void createAddOrderSearchDto(OrderDto dto, OrderLine orderLine) {
        OrderSearchDto orderSearchDto;
        orderSearchDto = new OrderSearchDto(dto.getDescription(), orderLine.getArticle().getDescription(), orderLine.getRequiredAmount(), orderLine.getFinalAmount(), dto.getOpeningDate(), dto.getClosingDate());
        orderSearchDtos.add(orderSearchDto);
    }

    private List<User> getUsersWithNotCommittedTickets(String code) {
        List<Ticket> Tickets = this.ticketRepository.findByShoppingListArticle(code);
        List<User> user = new ArrayList<>();
        for(Ticket item: Tickets) {
            LogManager.getLogger().debug("Articulo nombre " + item.getUser());
            for(Shopping article : item.getShoppingList()) {
                if(article.getShoppingState() == ShoppingState.NOT_COMMITTED) {
                    user.add(item.getUser());
                }
            }
        }
        return user;
    }
}
