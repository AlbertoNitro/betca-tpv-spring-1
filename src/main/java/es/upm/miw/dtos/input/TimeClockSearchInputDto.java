package es.upm.miw.dtos.input;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class TimeClockSearchInputDto {

    private LocalDateTime dateFrom;

    private LocalDateTime dateTo;

    @Pattern(regexp = es.upm.miw.dtos.validations.Pattern.NINE_DIGITS)
    private String userMobile;

    public TimeClockSearchInputDto() {
        // Empty for framework
    }

    public TimeClockSearchInputDto(LocalDateTime dateFrom, LocalDateTime dateTo, String userMobile) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.userMobile = userMobile;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    @Override
    public String toString() {
        return "TimeClockSearchInputDto{" +
                "dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", userMobile='" + userMobile + '\'' +
                '}';
    }
}
