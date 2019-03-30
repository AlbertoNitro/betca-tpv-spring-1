package es.upm.miw.dtos.output;

import es.upm.miw.documents.TimeClock;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeClockOutputDto {

    protected static final String TIME_FORMAT = "HH:mm:ss";
    protected static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter
            .ofPattern(TIME_FORMAT);

    @NotNull
    private String id;

    @NotNull
    private Long dateMs;

    @NotNull
    private String inMs;

    @NotNull
    private String outMs;

    @NotNull
    private Long totalHours;

    private String username;

    private String dni;

    @NotNull
    @Pattern(regexp = es.upm.miw.dtos.validations.Pattern.NINE_DIGITS)
    private String mobile;

    public TimeClockOutputDto() {
        // Empty for framework
    }

    public TimeClockOutputDto(TimeClock timeClock) {
        this.id = timeClock.getId();
        this.dateMs = getDateMs(timeClock.getClockinDate());
        this.inMs = getInMsFromClockinDate(timeClock.getClockinDate());
        this.outMs = getOutMsFromClockoutDate(timeClock.getClockoutDate());
        this.totalHours = timeClock.getTotalHours();
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

    private Long getDateMs(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        return dateTime.toLocalDate().atStartOfDay(zoneId).toInstant().toEpochMilli();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getDateMs() {
        return dateMs;
    }

    public void setDateMs(Long dateMs) {
        this.dateMs = dateMs;
    }

    public String getInMs() {
        return inMs;
    }

    public void setInMs(String inMs) {
        this.inMs = inMs;
    }

    public String getOutMs() {
        return outMs;
    }

    public void setOutMs(String outMs) {
        this.outMs = outMs;
    }

    public Long getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Long totalHours) {
        this.totalHours = totalHours;
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
                ", dateMs=" + dateMs +
                ", inMs='" + inMs + '\'' +
                ", outMs='" + outMs + '\'' +
                ", totalHours=" + totalHours +
                ", username='" + username + '\'' +
                ", dni='" + dni + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
