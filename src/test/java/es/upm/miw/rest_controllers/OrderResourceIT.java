package es.upm.miw.rest_controllers;

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
}
