package es.upm.miw.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketQueryInputDto {

    private String userMobile;

    private LocalDateTime dateStart;

    private LocalDateTime dateEnd;

    private BigDecimal totalMin;

    private BigDecimal totalMax;

    private Boolean pending;

    private String orderId;

    public TicketQueryInputDto() {
        //empty for quick creation
        this.pending = false;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDateTime getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDateTime dateEnd) {
        this.dateEnd = dateEnd;
    }

    public BigDecimal getTotalMin() {
        return totalMin;
    }

    public void setTotalMin(BigDecimal totalMin) {
        this.totalMin = totalMin;
    }

    public BigDecimal getTotalMax() {
        return totalMax;
    }

    public void setTotalMax(BigDecimal totalMax) {
        this.totalMax = totalMax;
    }

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "TicketQueryInputDto{" +
                "userMobile='" + userMobile +
                ", dateStart=" + dateStart +
                ", dateEnd='" + dateEnd +
                ", totalMin=" + totalMin +
                ", totalMax=" + totalMax +
                ", pending=" + pending +
                ", orderId=" + orderId +
                '}';
    }
}
