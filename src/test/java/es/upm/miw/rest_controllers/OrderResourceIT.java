package es.upm.miw.rest_controllers;

<<<<<<< HEAD
import es.upm.miw.business_controllers.OrderController;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Order;
import es.upm.miw.documents.OrderLine;
import es.upm.miw.documents.Provider;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.OrderRepository;
import es.upm.miw.repositories.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static java.sql.JDBCType.NULL;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@ApiTestConfig
public class OrderResourceIT {
    @Autowired
    private RestService restService;

    private Order order;

    private Provider provider;

    @Autowired
    private ProviderRepository providerRepository;

    private Article article;

    @Autowired
    private ArticleRepository articleRepository;

    private OrderController orderController = new OrderController();

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void before() {
        this.article = this.articleRepository.findAll().get(0);
        this.provider = this.providerRepository.findAll().get(0);
        OrderLine orderLine = new OrderLine(article, 10);
        OrderLine[] orderLines = {orderLine};
        this.order = new Order("Test Order", this.provider, orderLines);
        this.order = this.orderRepository.save(this.order);
    }

    @Test
    void testCloseOrder() {
        Order closedOrder = this.orderController.closeOrder(this.order);
        assertNotNull(closedOrder.getClosingDate());
    }
=======
import es.upm.miw.dtos.OrderSearchDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
public class OrderResourceIT {

    @Autowired
    private RestService restService;
    private OrderSearchDto existentOrder;

    @Test
    void testReadNotFound() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder(new RestBuilder<OrderSearchDto[]>()).clazz(OrderSearchDto[].class)
                        .path(OrderResource.ORDERS).path("/non-existent-id")
                        .get().build());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testReadAll() {
        List<OrderSearchDto> orders = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<OrderSearchDto[]>()).clazz(OrderSearchDto[].class)
                .path(OrderResource.ORDERS)
                .get().build());
        System.out.println("OrdersResource: " + orders);
        assertTrue(orders.size() >= 0);
    }

>>>>>>> 15ff062edd547971a0ff9c2ce840418e8ac73d15
}
