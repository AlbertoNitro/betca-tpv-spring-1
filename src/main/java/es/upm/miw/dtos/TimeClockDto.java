package es.upm.miw.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.documents.TimeClock;
import es.upm.miw.documents.User;

import java.time.LocalDateTime;

public class TimeClockDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime clockinDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime clockoutDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long totalHours;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User user;

    public TimeClockDto() {
        // Empty for framework
    }

    public TimeClockDto(TimeClock timeClock) {
        this.id = timeClock.getId();
        this.clockinDate = timeClock.getClockinDate();
        this.clockoutDate = timeClock.getClockoutDate();
        this.totalHours = timeClock.getTotalHours();
        this.user = timeClock.getUser();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getClockinDate() {
        return clockinDate;
    }

    public void setClockinDate(LocalDateTime clockinDate) {
        this.clockinDate = clockinDate;
    }

    public LocalDateTime getClockoutDate() {
        return clockoutDate;
    }

    public void setClockoutDate(LocalDateTime clockoutDate) {
        this.clockoutDate = clockoutDate;
    }

    public Long getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Long totalHours) {
        this.totalHours = totalHours;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TimeClockDto{" +
                "id='" + id + '\'' +
                ", clockinDate=" + clockinDate +
                ", clockoutDate=" + clockoutDate +
                ", totalHours=" + totalHours +
                ", user=" + user +
                '}';
    }

}
