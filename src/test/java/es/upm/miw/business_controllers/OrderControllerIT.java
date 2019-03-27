package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.dtos.OrderSearchDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class OrderControllerIT {

    @Autowired
    private OrderController orderController;

    @Test
    void testReadAll() {
        List<OrderSearchDto> orders = orderController.readAll();
        assertTrue(orders.size() >= 0);
    }
}
