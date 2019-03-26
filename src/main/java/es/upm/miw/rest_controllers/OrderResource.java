package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.OrderController;
import es.upm.miw.documents.Order;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;

public class OrderResource {
    public static final String ORDERS = "/orders";
    public static final String QUERY = "/query";
    public static final String ORDER_ID = "/orderId";

    OrderController orderController;


    public Order closeOrder(@NotNull @RequestBody Order order) {
        return this.orderController.closeOrder(order);
    }

}
