package es.upm.miw.dtos.output;

import es.upm.miw.documents.TimeClock;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeClockOutputDto {

    protected static final String TIME_FORMAT = "HH:mm:ss";
    protected static final String DATE_FORMAT = "dd/MM/yyyy";
    protected static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter
            .ofPattern(TIME_FORMAT);
    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern(DATE_FORMAT);

    @NotNull
    private String id;

    @NotNull
    private String date;

    @NotNull
    private String in;

    @NotNull
    private String out;

    @NotNull
    private Long total;

    private String username;

    private String dni;

    @NotNull
    @Pattern(regexp = es.upm.miw.dtos.validations.Pattern.NINE_DIGITS)
    private String mobile;

    public TimeClockOutputDto() {
        this.id = new ObjectId().toHexString();
    }

    public TimeClockOutputDto(TimeClock timeClock) {
        this.id = timeClock.getId();
        this.date = parserDateToString(timeClock.getClockinDate());
        this.in = getInMsFromClockinDate(timeClock.getClockinDate());
        this.out = getOutMsFromClockoutDate(timeClock.getClockoutDate());
        this.total = timeClock.getTotalHours();
        this.dni = timeClock.getUser().getDni();
        this.username = timeClock.getUser().getUsername();
        this.mobile = timeClock.getUser().getMobile();
    }

    private String getOutMsFromClockoutDate(LocalDateTime clockoutDate) {
        return clockoutDate.toLocalTime().format(TIME_FORMATTER);
    }

    private String getInMsFromClockinDate(LocalDateTime clockinDate) {
        return clockinDate.toLocalTime().format(TIME_FORMATTER);
    }

    private String parserDateToString(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        return dateTime.toLocalDate().atStartOfDay(zoneId).format(DATE_FORMATTER);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "TimeClockOutputDto{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", in='" + in + '\'' +
                ", out='" + out + '\'' +
                ", total=" + total +
                ", username='" + username + '\'' +
                ", dni='" + dni + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
