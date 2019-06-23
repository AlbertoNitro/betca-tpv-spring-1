package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.*;
import es.upm.miw.dtos.*;
import es.upm.miw.dtos.OrderDto;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.OrderRepository;
import es.upm.miw.repositories.ProviderRepository;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class OrderControllerIT {

    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ProviderController providerController;

    @Autowired
    private ProviderRepository providerRepository;

    private String idOrder = "";

    @BeforeEach
    void createOrder() {
        if (this.orderRepository.findAll().size() == 0) {
            String[] articlesId = {"1", "8400000000048", "8400000000024", "8400000000031"};
            String description = "ORDER-" + String.valueOf((int) (Math.random() * 10000));
            for (int i = 0; i < 3; i++) {
                Article article = this.articleRepository.findById(articlesId[i]).get();
                OrderLine[] orderLines = Arrays.array(new OrderLine(article, 4), new OrderLine(article, 5));
                if (i > 1 && i < 3) {
                    description = "ORDER-02019";
                }
                Order order = new Order(description, article.getProvider(), orderLines);
                this.orderRepository.save(order);
                idOrder = order.getId();
            }
        }
    }

    @Test
    void testFindById() {
        Optional<Order> order = this.orderRepository.findByDescription("ORDER-02019");
        Optional<Order> order2 = this.orderRepository.findById(order.get().getId());
        assertTrue(order.isPresent());
    }

    @Test
    void testReadAll() {
        List<OrderSearchDto> orders = orderController.readAll();
        assertTrue(orders.size() >= 0);
    }

    @Test
    void testSearch() {
        List<OrderSearchDto> orders = orderController.searchOrder("OrderDescrip_8400000000024", "", true);
        assertTrue(orders.size() >= 0);
    }

    @Test
    void testUsersWithArticleReserved() {
        Optional<Order> order = this.orderRepository.findByDescription("ORDER-02019");
        List<User> users = this.orderController.sendArticlesFromOrderLine(order.get().getOrderLines());
        Provider provider = new Provider(new ProviderDto("new-company: " + String.valueOf((int) (Math.random() * 10000))));
        this.providerRepository.save(provider);
        Order order1 = new Order("ORDER-12389", provider, order.get().getOrderLines());
        this.orderRepository.save(order1);
        assertTrue(users.size() > 0);
        this.orderRepository.delete(order1);
    }

    @Test
    void testCreate() {
        OrderDto orderDto = new OrderDto();
        String[] articlesId = {"1", "8400000000017", "8400000000024", "8400000000031"};
        Integer[] requiredAmount = {1, 2, 3, 4};
        Provider provider = new Provider(new ProviderDto("new-company: " + String.valueOf((int) (Math.random() * 10000))));
        this.providerRepository.save(provider);
        orderDto = this.orderController.create("ORDER-" + String.valueOf((int) (Math.random() * 10000)), provider.getId(), articlesId, requiredAmount);
        assertTrue(orderDto.getOrderLines().length > 0);
    }

    @Test
    void testGetOrdereById() {
        OrderDto orderDto = this.orderController.getOrderById(idOrder);
        assertEquals(orderDto.getId(), idOrder);
    }

    @Test
    void testDeleteOrderExistController() {
        this.orderController.delete(idOrder);
    }
}
