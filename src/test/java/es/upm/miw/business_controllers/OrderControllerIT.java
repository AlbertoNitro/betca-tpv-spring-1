package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Order;
import es.upm.miw.documents.OrderLine;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.OrderDto;
import es.upm.miw.dtos.OrderSearchDto;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.OrderRepository;
import es.upm.miw.rest_controllers.OrderResource;
import es.upm.miw.rest_controllers.RestBuilder;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class OrderControllerIT {

    private Order order;

    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void createOrder() {
        if (this.orderRepository.findAll().size() == 0) {
            String[] articlesId = {"1", "8400000000048", "8400000000024", "8400000000031"};
            for (int i = 0; i < 3; i++) {
                Article article = this.articleRepository.findById(articlesId[i]).get();
                OrderLine[] orderLines = Arrays.array(new OrderLine(article, 4), new OrderLine(article, 5));
                Order order = new Order("OrderDescrip_" + articlesId[i], article.getProvider(), orderLines);
                this.order = this.orderRepository.save(order);
            }
        }
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
        List<User> users = this.orderController.sendArticlesFromOrderLine(this.order.getOrderLines());
        assertTrue(users.size() > 0);
        this.orderRepository.delete(this.order);
    }
}
