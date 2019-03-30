package es.upm.miw.documents;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Document
public class TimeClock {
    @Id
    private String id;

    private LocalDateTime clockinDate;

    private LocalDateTime clockoutDate;

    private Long totalHours;

    @DBRef
    private User user;

    public TimeClock() {
        this.id = new ObjectId().toHexString();
    }

    public TimeClock(User user) {
        this();
        clockin();
        clockout();
        this.user = user;
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

    public void clockin() {
        this.clockinDate = LocalDateTime.now();
    }

    public void clockout() {
        this.clockoutDate = LocalDateTime.now();
        this.totalHours = Duration.between(this.clockoutDate, this.clockinDate).toHours();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeClock timeClock = (TimeClock) o;
        return Objects.equals(id, timeClock.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "TimeClock{" +
                "id='" + id + '\'' +
                ", clockinDate=" + clockinDate +
                ", clockoutDate=" + clockoutDate +
                ", totalHours=" + totalHours +
                ", user=" + user +
                '}';
    }

}
