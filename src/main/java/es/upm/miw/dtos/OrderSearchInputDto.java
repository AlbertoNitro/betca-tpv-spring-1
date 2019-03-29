package es.upm.miw.dtos;

public class OrderSearchInputDto {

    private String descriptionOrders;

    private String descriptionArticles;

    private boolean onlyClosingDate;

    public OrderSearchInputDto() {
        // Empty for framework
    }

    public OrderSearchInputDto(String descriptionOrders, String descriptionArticles, boolean onlyClosingDate) {
        this.descriptionOrders = descriptionOrders;
        this.descriptionArticles = descriptionArticles;
        this.onlyClosingDate = onlyClosingDate;
    }

    public String getDescriptionOrders() {
        return descriptionOrders;
    }

    public String getDescriptionArticles() {
        return descriptionArticles;
    }

    public boolean isOnlyClosingDate() {
        return onlyClosingDate;
    }

    public void setDescriptionOrders(String descriptionOrders) {
        this.descriptionOrders = descriptionOrders;
    }

    public void setDescriptionArticles(String descriptionArticles) {
        this.descriptionArticles = descriptionArticles;
    }

    public void setOnlyClosingDate(boolean onlyClosingDate) {
        this.onlyClosingDate = onlyClosingDate;
    }

    @Override
    public String toString() {
        return "OrderSearchInputDto{" +
                "descriptionOrders='" + descriptionOrders + '\'' +
                ", descriptionArticles='" + descriptionArticles + '\'' +
                ", onlyClosingDate=" + onlyClosingDate +
                '}';
    }
}
