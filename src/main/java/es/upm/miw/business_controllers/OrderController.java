package es.upm.miw.business_controllers;

import es.upm.miw.documents.OrderLine;
import es.upm.miw.dtos.OrderDto;
import es.upm.miw.dtos.OrderSearchDto;
import es.upm.miw.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

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
}
