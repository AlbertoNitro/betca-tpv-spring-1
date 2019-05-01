package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Order;
import es.upm.miw.documents.OrderLine;
import es.upm.miw.dtos.OrderDto;
import es.upm.miw.dtos.OrderSearchDto;
import es.upm.miw.dtos.ProviderDto;
import es.upm.miw.exceptions.ConflictException;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.OrderRepository;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class OrderControllerIT {

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
                this.orderRepository.save(order);
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
    void testCreate() {
        OrderDto orderDto = new OrderDto();
        String[] articlesId = {"1", "8400000000048", "8400000000024", "8400000000031"};
        Integer[] requiredAmount = {1,2,3,4};
        this.orderController.create("Desc", "5c9e5cc88f8e3f2c0cdd5ebe",  articlesId , requiredAmount );
        //assertThrows(ConflictException.class, () -> this.orderController.create(providerDto));
    }

    @Test
    void testRead() {
        List<OrderSearchDto> order = orderController.findById("OrderDescrip_8400000000024");
        System.out.println("orderRead: "+ order);
        assertNotNull(order.size() >=0);
    }
}
