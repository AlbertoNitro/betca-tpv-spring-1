package es.upm.miw.rest_controllers;

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

}
