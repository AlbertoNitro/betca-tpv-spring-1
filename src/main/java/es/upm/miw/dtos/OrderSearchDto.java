package es.upm.miw.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.time.LocalDateTime;

@JsonInclude(Include.NON_NULL)
public class OrderSearchDto {

    private String id;

    private String descriptionOrders;

    private String descriptionArticles;

    private int requiredAmount;

    private int finalAmount;

    private LocalDateTime openingDate;

    private LocalDateTime closingDate;

    public OrderSearchDto() {
        // Empty for framework
    }

    public OrderSearchDto(String id, String descriptionOrders, String descriptionArticles, int requiredAmount, int finalAmount, LocalDateTime openingDate, LocalDateTime closingDate) {
        this.id = id;
        this.descriptionOrders = descriptionOrders;
        this.descriptionArticles = descriptionArticles;
        this.requiredAmount = requiredAmount;
        this.finalAmount = finalAmount;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
    }

    public OrderSearchDto(String descriptionOrders, String descriptionArticles, int requiredAmount, int finalAmount, LocalDateTime openingDate, LocalDateTime closingDate) {
        this.descriptionOrders = descriptionOrders;
        this.descriptionArticles = descriptionArticles;
        this.requiredAmount = requiredAmount;
        this.finalAmount = finalAmount;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
    }

    public String getDescriptionOrders() {
        return descriptionOrders;
    }

    public String getDescriptionArticles() {
        return descriptionArticles;
    }

    public int getRequiredAmount() {
        return requiredAmount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public LocalDateTime getOpeningDate() {
        return openingDate;
    }

    public LocalDateTime getClosingDate() {
        return closingDate;
    }

    public void setDescriptionOrders(String descriptionOrders) {
        this.descriptionOrders = descriptionOrders;
    }

    public void setDescriptionArticles(String descriptionArticles) {
        this.descriptionArticles = descriptionArticles;
    }

    public void setRequiredAmount(int requiredAmount) {
        this.requiredAmount = requiredAmount;
    }

    public void setFinalAmount(int finalAmount) {
        this.finalAmount = finalAmount;
    }

    public void setOpeningDate(LocalDateTime openingDate) {
        this.openingDate = openingDate;
    }

    public void setClosingDate(LocalDateTime closingDate) {
        this.closingDate = closingDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OrderSearchDto{" +
                "id='" + id + '\'' +
                ", descriptionOrders='" + descriptionOrders + '\'' +
                ", descriptionArticles='" + descriptionArticles + '\'' +
                ", requiredAmount=" + requiredAmount +
                ", finalAmount=" + finalAmount +
                ", openingDate=" + openingDate +
                ", closingDate=" + closingDate +
                '}';
    }
}
