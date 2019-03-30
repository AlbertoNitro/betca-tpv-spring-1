package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.OrderController;
import es.upm.miw.documents.Order;
import es.upm.miw.documents.OrderLine;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.constraints.NotNull;
import es.upm.miw.dtos.OrderSearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(OrderResource.ORDERS)

public class OrderResource {

    @Autowired
    private OrderController orderController;

    public static final String ORDERS = "/orders";
    public static final String CLOSE = "/orders/close";
    public static final String ID = "/{id}";

    public Order closeOrder(@NotNull @RequestBody String orderId, OrderLine[] orderLine) {
        return this.orderController.closeOrder(orderId, orderLine);
    }


    @GetMapping
    public List<OrderSearchDto> readAll() {
        return this.orderController.readAll();
    }
}


