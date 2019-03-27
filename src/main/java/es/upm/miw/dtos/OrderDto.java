package es.upm.miw.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import es.upm.miw.documents.Order;
import es.upm.miw.documents.OrderLine;
import es.upm.miw.documents.Provider;

import java.time.LocalDateTime;
import java.util.Arrays;

@JsonInclude(Include.NON_NULL)
public class OrderDto {

    private String description;

    private Provider provider;

    private LocalDateTime openingDate;

    private LocalDateTime closingDate;

    private OrderLine[] orderLines;

    public OrderDto() {
        // Empty for framework
    }

    public OrderDto(Order order) {
        this.openingDate = order.getOpeningDate();
        this.description = order.getDescription();
        this.provider = order.getProvider();
        this.orderLines = order.getOrderLines();
    }

    public String getDescription() {
        return description;
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

    public OrderLine[] getOrderLines() {
        return orderLines;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public void setOpeningDate(LocalDateTime openingDate) {
        this.openingDate = openingDate;
    }

    public void setClosingDate(LocalDateTime closingDate) {
        this.closingDate = closingDate;
    }

    public void setOrderLines(OrderLine[] orderLines) {
        this.orderLines = orderLines;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "description='" + description + '\'' +
                ", provider=" + provider +
                ", openingDate=" + openingDate +
                ", closingDate=" + closingDate +
                ", orderLines=" + Arrays.toString(orderLines) +
                '}';
    }
}
