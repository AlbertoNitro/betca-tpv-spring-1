package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.*;
import es.upm.miw.dtos.*;
import es.upm.miw.dtos.OrderDto;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.OrderRepository;
import es.upm.miw.repositories.ProviderRepository;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
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

    @Autowired
    private ProviderController providerController;

    @Autowired
    private ProviderRepository providerRepository;

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

    @Test
    void testCreate() {
        OrderDto orderDto = new OrderDto();
        String[] articlesId = {"1", "8400000000017", "8400000000024", "8400000000031"};
        Integer[] requiredAmount = {1, 2, 3, 4};
        Provider provider = new Provider(new ProviderDto("new-companyFred"));
        this.providerRepository.save(provider);
        orderDto = this.orderController.create("Desc", provider.getId(), articlesId, requiredAmount);
        assertTrue(orderDto.getOrderLines().length > 0);
    }
}
