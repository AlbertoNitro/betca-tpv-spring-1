package es.upm.miw.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Arrays;

@Document
public class Order {

    @Id
    private String id;

    private String description;

    @DBRef
    private final Provider provider;

    private final LocalDateTime openingDate;

    private LocalDateTime closingDate;

    private OrderLine[] orderLine;

    public Order(String description, Provider provider, OrderLine[] orderLine) {
        this.openingDate = LocalDateTime.now();
        this.description = description;
        this.provider = provider;
        this.orderLine = orderLine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderLine[] getOrderLine() {
        return orderLine;
    }

    public void setOrderLine(OrderLine[] orderLine) {
        this.orderLine = orderLine;
    }

    public String getId() {
        return id;
    }

    public Provider getProvider() {
        return provider;
    }

    public LocalDateTime getOpeningDate() {
        return openingDate;
    }

    public LocalDateTime getClosingDate() {
        return closingDate;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && id.equals(((Order) obj).id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", provider=" + provider +
                ", openingDate=" + openingDate +
                ", closingDate=" + closingDate +
                ", orderLine=" + Arrays.toString(orderLine) +
                '}';
    }

    public void close() {
        this.closingDate = LocalDateTime.now();
    }

}
