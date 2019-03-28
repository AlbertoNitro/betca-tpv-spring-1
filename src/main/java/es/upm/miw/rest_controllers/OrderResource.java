package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.OrderController;
<<<<<<< HEAD
import es.upm.miw.documents.Order;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;

=======
import es.upm.miw.dtos.OrderSearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(OrderResource.ORDERS)
>>>>>>> 15ff062edd547971a0ff9c2ce840418e8ac73d15
public class OrderResource {

    public static final String ORDERS = "/orders";
    public static final String ID = "/{id}";

<<<<<<< HEAD
    OrderController orderController;


    public Order closeOrder(@NotNull @RequestBody Order order) {
        return this.orderController.closeOrder(order);
    }
=======
    @Autowired
    private OrderController orderController;
>>>>>>> 15ff062edd547971a0ff9c2ce840418e8ac73d15

    @GetMapping
    public List<OrderSearchDto> readAll() {
        return this.orderController.readAll();
    }
}


