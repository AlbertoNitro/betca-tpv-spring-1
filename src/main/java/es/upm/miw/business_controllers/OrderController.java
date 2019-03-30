package es.upm.miw.business_controllers;

import es.upm.miw.documents.Order;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import es.upm.miw.documents.OrderLine;
import es.upm.miw.dtos.OrderDto;
import es.upm.miw.dtos.OrderSearchDto;
import org.springframework.stereotype.Controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    public Order closeOrder(String orderId, OrderLine[] orderLine) {
        Order closeOrder = orderRepository.findById(orderId).orElse(null);
        if(closeOrder.getOrderLines().length > 0) {
            closeOrder.close();
            closeOrder.setOrderLines(orderLine);
            closeOrder = orderRepository.save(closeOrder);
        } else {
            throw new BadRequestException("orderLine is empty");
        }

        return closeOrder;
    }

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
