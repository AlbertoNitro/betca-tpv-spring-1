package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.OrderController;
import es.upm.miw.dtos.*;
import es.upm.miw.exceptions.BadRequestException;
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
    public static final String CLOSE = "/close";
    public static final String ORDERS_art = "/art";
    public static final String ARTICLE = "/article";
    public static final String ORDER_ID = "/{idOrder}";
    public static final String ORDER = "/order";

    @Autowired
    private OrderController orderController;

    @Autowired
    private ProviderRepository providerRepository;

    @PostMapping(value = CLOSE)
    public List<OrderDto> closeOrder(@Valid @RequestBody OrderDto orderDto) {
        System.out.println("Order: " + orderDto);
        List<OrderDto> closedOrder = new ArrayList<>();
        if(orderDto.getOrderLines() == null) {
            throw new BadRequestException("orderLine is empty");
        } else {
            closedOrder.add(new OrderDto(this.orderController.closeOrder(orderDto.getId(), orderDto.getOrderLines())));
        }
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
        String desc= "ORDER-" + String.valueOf((int) (Math.random() * 10000));
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

    //find by Id de una orden de compra
    @PostMapping(value = ARTICLE)
    public List<OrderArticleDto> findById(@Valid @RequestBody  OrderDto orderDto) {
        System.out.println("find By id: " + orderDto.getId());
        return this.orderController.findById(orderDto.getId());
    }

    //Get by Id de una orden de compra
    @PostMapping(value = ORDER)
    public OrderDto getOrderById(@Valid @RequestBody  OrderDto orderDto) {
        return this.orderController.getOrderById(orderDto.getId());
    }

    @DeleteMapping(value = ORDER_ID)
    public void delete(@PathVariable String idOrder) {
        this.orderController.delete(idOrder);
    }
}