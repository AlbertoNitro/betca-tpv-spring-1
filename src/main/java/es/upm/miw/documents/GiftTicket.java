package es.upm.miw.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
public class GiftTicket {

    @Id
    private String id;

    private LocalDate creationDate;

    private LocalDate expirationDate;

    private String note;

    public GiftTicket() {
        this.creationDate = LocalDate.now();
        this.expirationDate = creationDate.plusDays(14);
    }

    public GiftTicket(String id, String note) {
        this();
        this.id = id;
        this.note = "" + note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "GiftTicket{" +
                "id='" + id + '\'' +
                ", creationDate=" + creationDate +
                ", expirationDate=" + expirationDate +
                ", note='" + note + '\'' +
                '}';
    }
}
