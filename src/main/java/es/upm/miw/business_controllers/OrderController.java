package es.upm.miw.business_controllers;

import es.upm.miw.documents.Article;
import es.upm.miw.documents.Order;
import es.upm.miw.documents.OrderLine;
import es.upm.miw.documents.Provider;
import es.upm.miw.dtos.OrderDto;
import es.upm.miw.dtos.OrderSearchDto;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.OrderRepository;
import es.upm.miw.repositories.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    private List<OrderSearchDto> orderSearchDtos;



    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ProviderRepository providerRepository;

    public static String SEARCHWORD = "";

    public OrderDto create(String descriptionOrder, String providerId, String[] idArticles, Integer[] requiredAmount) {

        OrderLine[] orderLines = new OrderLine[idArticles.length];
        for (int i = 0; i < idArticles.length; i++) {
            Article article = this.articleRepository.findById(idArticles[i]).get();
            orderLines[i] = new OrderLine(article, requiredAmount[i]);
        }
        Provider provider = providerRepository.findById(providerId).get();
        Order order = new Order(descriptionOrder, provider, orderLines);
        this.orderRepository.save(order);
        return new OrderDto(order);
    }

    public List<OrderSearchDto> findById(String id){
        OrderSearchDto orderSearchDto = null;
        orderSearchDtos = new ArrayList<>();
        Optional<Order> order= orderRepository.findByDescription(id);
        for (OrderLine orderLine : order.get().getOrderLines()) {
            orderSearchDto = new OrderSearchDto(order.get().getDescription(), orderLine.getArticle().getDescription(), orderLine.getRequiredAmount(), orderLine.getFinalAmount(), order.get().getOpeningDate(), order.get().getClosingDate());
            orderSearchDtos.add(orderSearchDto);
        }
       return orderSearchDtos;
    }

 /*   public OrderDto update(String id, OrderDto providerDto) {

        String company = providerDto.getCompany();
        Optional<Provider> provider = this.providerRepository.findByCompany(company);
        if (provider.isPresent() && !provider.get().getId().equals(id))
            throw new ConflictException("Provider company (" + company + ")");
        Provider result = this.providerRepository.save(new Provider(providerDto));
        return new ProviderDto(result);
    }*/

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
}
