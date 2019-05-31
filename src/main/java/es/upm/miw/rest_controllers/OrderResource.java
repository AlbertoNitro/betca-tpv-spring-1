package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.OrderController;
import es.upm.miw.dtos.OrderDto;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.constraints.NotNull;
import es.upm.miw.dtos.OrderSearchDto;
import es.upm.miw.dtos.OrderSearchInputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(OrderResource.ORDERS)

public class OrderResource {
    public static final String ORDERS = "/orders";
    public static final String ID = "/{id}";
    public static final String SEARCH = "/search";
    public static final String CLOSE = "/orders/close/{id}";

    @Autowired
    private OrderController orderController;

    @PostMapping(CLOSE)
    public List<OrderDto> closeOrder(@NotNull @RequestBody OrderDto orderDto) {
        List<OrderDto> closedOrder = new ArrayList<>();
        closedOrder.add(new OrderDto(this.orderController.closeOrder(orderDto.getId(), orderDto.getOrderLines())));
        return closedOrder;
    }


    @GetMapping
    public List<OrderSearchDto> readAll() {
        return this.orderController.readAll();
    }

    @PostMapping(value = SEARCH)
    public List<OrderSearchDto> findByAttributesLike(@Valid @RequestBody OrderSearchInputDto orderSearchInputDto) {
        String descriptionOrders = orderSearchInputDto.getDescriptionOrders();
        String descriptionArticles = orderSearchInputDto.getDescriptionArticles();
        Boolean onlyClosingDate = orderSearchInputDto.isOnlyClosingDate();
        return this.orderController.searchOrder(descriptionOrders, descriptionArticles, onlyClosingDate);
    }
}


