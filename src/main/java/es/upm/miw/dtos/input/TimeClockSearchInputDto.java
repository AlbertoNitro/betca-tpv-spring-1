package es.upm.miw.dtos.input;

import es.upm.miw.exceptions.BadRequestException;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static es.upm.miw.dtos.validations.Pattern.NINE_DIGITS;

public class TimeClockSearchInputDto {

    private LocalDateTime dateFrom;

    private LocalDateTime dateTo;

    @Valid
    @Pattern(regexp = NINE_DIGITS)
    private String userMobile;

    public TimeClockSearchInputDto() {
        // Empty for framework
    }

    public TimeClockSearchInputDto(Long dateFromMs, Long dateToMs, String mobile) {
        this.dateFrom = getLocalDateTimeFromMs(dateFromMs);
        this.dateTo = getLocalDateTimeFromMs(dateToMs);
        this.userMobile = mobile;
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

    private LocalDateTime getLocalDateTimeFromMs(Long dateMs) {
        return dateMs != null ? Instant.ofEpochMilli(dateMs).atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
    }

    public void validate() throws BadRequestException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
            throw new BadRequestException(
                    String.format("Date From '%s' is after than Date To '%s'. It is not a valid date range for the query",
                            dateFrom.format(formatter), dateTo.format(formatter)));
        }
        if (!StringUtils.isEmpty(userMobile) && !userMobile.matches(NINE_DIGITS)) {
            throw new BadRequestException(
                    String.format("User Mobile '%s' is not a valid mobile for the query",
                            userMobile));
        }
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
