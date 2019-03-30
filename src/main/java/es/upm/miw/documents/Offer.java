package es.upm.miw.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Arrays;

@Document
public class Offer {

    @Id
    private String id;

    private String offername;

    private LocalDateTime endDate;

    private ArticleLine[] articleLine;

    public Offer(String id, String offername, LocalDateTime endDate, ArticleLine[] articleLine) {
        this.id = id;
        this.offername = offername;
        this.endDate = endDate;
        this.articleLine = articleLine;
    }

    public Offer(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOffername() {
        return offername;
    }

    public void setOffername(String offername) {
        this.offername = offername;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public ArticleLine[] getArticleLine() {
        return articleLine;
    }

    public void setArticleLine(ArticleLine[] articleLine) {
        this.articleLine = articleLine;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id='" + id + '\'' +
                ", offername='" + offername + '\'' +
                ", endDate=" + endDate +
                ", articleLine=" + Arrays.toString(articleLine) +
                '}';
    }
}
