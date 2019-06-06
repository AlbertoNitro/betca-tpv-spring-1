package es.upm.miw.rest_controllers;

import es.upm.miw.documents.*;
import es.upm.miw.business_controllers.OrderController;
import es.upm.miw.dtos.*;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.OrderRepository;
import es.upm.miw.repositories.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
public class OrderResourceIT {

    private Order order;

    private Provider provider;

    @Autowired
    private ProviderRepository providerRepository;

    private Article article;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private RestService restService;

    private OrderSearchDto existentOrder;

    @BeforeEach
    void createOrder() {
        if (this.orderRepository.findAll().size() == 0) {
            String[] articlesId = {"1", "8400000000048", "8400000000024", "8400000000031"};
            for (int i = 0; i < 3; i++) {
                Article article = this.articleRepository.findById(articlesId[i]).get();
                OrderLine[] orderLines = org.assertj.core.util.Arrays.array(new OrderLine(article, 4), new OrderLine(article, 5));
                Order order = new Order("OrderDescrip_" + articlesId[i], article.getProvider(), orderLines);
                this.orderRepository.save(order);
            }
        }
    }

    @BeforeEach
    void before() {
        this.article = this.articleRepository.findAll().get(1);
        this.article.setStock(0);
        this.provider = this.providerRepository.findAll().get(0);
        OrderLine orderLine = new OrderLine(article, 10);
        OrderLine[] orderLines = {orderLine};
        this.order = new Order("Test Order", this.provider, orderLines);
        this.order = this.orderRepository.save(this.order);
    }

    /*@Test
    void testCloseOrder() {
        OrderDto orderDto = new OrderDto(this.order);
        OrderDto[] closedOrder = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<OrderDto[]>()).clazz(OrderDto[].class)
                .path(OrderResource.ORDERS).path(OrderResource.CLOSE)
                .body(orderDto).post().build();
        assertNotNull(closedOrder[0].getClosingDate());
        this.orderRepository.delete(this.order);
    }*/

    @Test
    void testEmptyOrderLine() {
        OrderLine[] orderLines = {};
        this.order.setOrderLines(orderLines);
        OrderDto orderDto = new OrderDto(this.order);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> this.restService.loginAdmin()
                .restBuilder(new RestBuilder<OrderDto[]>())
                .clazz(OrderDto[].class)
                .path(OrderResource.ORDERS).path(OrderResource.CLOSE)
                .body(orderDto).post().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        this.orderRepository.delete(this.order);
    }

    @Test
    void testReadAll() {
        List<OrderSearchDto> orders = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<OrderSearchDto[]>())
                .clazz(OrderSearchDto[].class)
                .path(OrderResource.ORDERS)
                .get()
                .build());
        assertTrue(orders.size() >= 0);
    }

    @Test
    void testFindByAttributesLike() {
        OrderSearchInputDto orderSearchInputDto = new OrderSearchInputDto("null", "null", false);
        List<OrderSearchDto> activesSearch = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<OrderSearchDto[]>())
                .clazz(OrderSearchDto[].class)
                .path(OrderResource.ORDERS)
                .path(OrderResource.SEARCH)
                .body(orderSearchInputDto)
                .post().build());
        assertTrue(activesSearch.size() >= 0);

    }

}
