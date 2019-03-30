package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Order;
import es.upm.miw.documents.OrderLine;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class OrderRepositoryIT {

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
        Article article = this.articleRepository.findById("8400000000017").get();
        OrderLine[] orderLines = Arrays.array(new OrderLine(article, 4));
        Order order = new Order("Order 2Ds3Rf4", article.getProvider(), orderLines);
        this.orderRepository.save(order);
        assertTrue(this.orderRepository.findAll().size() > 0);

        Order dbOrder = this.orderRepository.findAll().stream()
                .filter(ord -> "Order 2Ds3Rf4".equals(ord.getDescription())).findFirst().get();

        assertNotNull(dbOrder.getOpeningDate());
        assertNull(dbOrder.getClosingDate());
        assertEquals("company-p1", dbOrder.getProvider().getCompany());
        assertEquals(1, dbOrder.getOrderLines().length);
        assertEquals(new Integer(4), dbOrder.getOrderLines()[0].getRequiredAmount());
        assertEquals(new Integer(4), dbOrder.getOrderLines()[0].getFinalAmount());

        dbOrder.setDescription("new description");
        dbOrder.close();

        assertNotNull(dbOrder.getClosingDate());

        this.orderRepository.delete(order);
    }

    @Test
    void readAllOrders() {
        assertTrue(orderRepository.findAllOrders().size() >= 0);
    }

}
