package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.OrderController;
import es.upm.miw.dtos.*;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.constraints.NotNull;
import es.upm.miw.repositories.ProviderRepository;

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
    public static final String ORDERS_art = "/art";

    @Autowired
    private OrderController orderController;

    @Autowired
    private ProviderRepository providerRepository;

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

    @PostMapping()
    public OrderDto createOrder(@Valid @RequestBody  OrderArticleDto[] articleDto ) {
        int size = articleDto.length;
        String[] articlesId = new String[size];
        Integer[] requiredAmount = new Integer[size];
        String desc= "Descr-" + String.valueOf((int) (Math.random() * 10000));
        String idProvider= "";
        int i=0;
        for (OrderArticleDto dto : articleDto) {
            articlesId[i] = dto.getCode();
            requiredAmount[i] = dto.getAmount();
            idProvider = dto.getProvider();
            i++;
        }
        return this.orderController.create(desc, idProvider, articlesId, requiredAmount);
    }

    @GetMapping(value = ID)
    public List<OrderSearchDto> read(@PathVariable String id) {
        return this.orderController.findByDescription(id);
    }
}


