package es.upm.miw.dtos;

import es.upm.miw.documents.TimeClock;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class TimeClockDto {

    @NotNull
    private String id;

    @NotNull
    private LocalDateTime clockinDate;

    @NotNull
    private LocalDateTime clockoutDate;

    @NotNull
    private Long totalHours;

    @NotNull
    private UserMinimumDto user;

    public TimeClockDto() {
        // Empty for framework
    }

    public TimeClockDto(TimeClock timeClock) {
        this.id = timeClock.getId();
        this.clockinDate = timeClock.getClockinDate();
        this.clockoutDate = timeClock.getClockoutDate();
        this.totalHours = timeClock.getTotalHours();
        this.user = new UserMinimumDto(timeClock.getUser().getMobile(), timeClock.getUser().getUsername());
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

    public UserMinimumDto getUser() {
        return user;
    }

    public void setUser(UserMinimumDto user) {
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
