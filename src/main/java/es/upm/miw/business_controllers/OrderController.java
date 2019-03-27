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

    public List<OrderSearchDto> readAll() {
        List<OrderSearchDto> orderSearchDtos=  new ArrayList<>();
        for (OrderDto dto : orderRepository.findAllOrders()) {
            for (OrderLine orderLine : dto.getOrderLines()) {
                OrderSearchDto orderSearchDto ;
                orderSearchDto=new OrderSearchDto(dto.getDescription(),orderLine.getArticle().getDescription(),orderLine.getRequiredAmount(),orderLine.getFinalAmount(),dto.getOpeningDate(),dto.getClosingDate());
                orderSearchDtos.add(orderSearchDto);
                System.out.println("orderSearchDto: " + orderSearchDto);
                System.out.println("order line: " + orderLine);
            }
        }
        System.out.println(",,,,,,,,,,;"+ orderSearchDtos);
        return orderSearchDtos ;
    }
}
