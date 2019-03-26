package es.upm.miw.business_controllers;

import es.upm.miw.documents.Order;
import es.upm.miw.documents.OrderLine;
import es.upm.miw.documents.Provider;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.repositories.OrderRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    public Order closeOrder(Order order) {

        if(order.getOrderLines().length > 0) {
            order.close();
            this.orderRepository.save(order);
        } else {
            throw new BadRequestException("orderLine is empty");
        }

        return order;
    }

}
