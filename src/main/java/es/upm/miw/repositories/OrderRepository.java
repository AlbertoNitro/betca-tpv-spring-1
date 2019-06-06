package es.upm.miw.repositories;

import es.upm.miw.documents.Order;
import es.upm.miw.dtos.OrderDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {

    @Query(value = "{}", fields = "{ 'id' : 1, 'description' : 1, 'provider': 1, 'openingDate': 1, 'closingDate':1, 'orderLines': 1}")
    List<OrderDto> findAllOrders();

    Optional<Order> findByDescription(String description);

}
