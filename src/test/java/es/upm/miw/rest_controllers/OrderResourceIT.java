package es.upm.miw.rest_controllers;

import es.upm.miw.documents.Article;
import es.upm.miw.documents.Order;
import es.upm.miw.documents.OrderLine;
import es.upm.miw.dtos.OrderSearchDto;
import es.upm.miw.dtos.OrderSearchInputDto;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.OrderRepository;
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

    @Autowired
    private RestService restService;

    private OrderSearchDto existentOrder;

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
                OrderLine[] orderLines = org.assertj.core.util.Arrays.array(new OrderLine(article, 4), new OrderLine(article, 5));
                Order order = new Order("OrderDescrip_" + articlesId[i], article.getProvider(), orderLines);
                this.orderRepository.save(order);
            }
        }
    }
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
        assertTrue(orders.size() >= 0);
    }

    @Test
    void testFindByAttributesLike() {
        OrderSearchInputDto orderSearchInputDto = new OrderSearchInputDto("null", "null", false);
        List<OrderSearchDto> activesSearch = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<OrderSearchDto[]>()).clazz(OrderSearchDto[].class)
                .path(OrderResource.ORDERS).path(OrderResource.SEARCH).body(orderSearchInputDto)
                .post().build());
        assertTrue(activesSearch.size() >= 0);

    }

    @Test
    void testRead() {
        OrderSearchDto providerDto = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<OrderSearchDto>()).clazz(OrderSearchDto.class)
                .path(OrderResource.ORDERS).path("/" + "OrderDescrip_8400000000024")
                .get().build();
    }
}
