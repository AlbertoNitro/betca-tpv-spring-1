package es.upm.miw.business_controllers;

<<<<<<< HEAD
import es.upm.miw.documents.Order;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.repositories.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

=======
import es.upm.miw.documents.OrderLine;
import es.upm.miw.dtos.OrderDto;
import es.upm.miw.dtos.OrderSearchDto;
import es.upm.miw.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import java.util.ArrayList;
import java.util.List;

@Controller
>>>>>>> 15ff062edd547971a0ff9c2ce840418e8ac73d15
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

<<<<<<< HEAD
    public Order closeOrder(Order order) {
        if(order.getOrderLines().length > 0) {
            order.close();
            orderRepository.save(order);
        } else {
            throw new BadRequestException("orderLine is empty");
        }

        return order;
    }

=======
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
>>>>>>> 15ff062edd547971a0ff9c2ce840418e8ac73d15
}
