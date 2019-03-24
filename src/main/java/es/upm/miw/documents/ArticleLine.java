package es.upm.miw.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ArticleLine {

    @Id
    private String idArticle;

    private Integer percentage;

    public ArticleLine(String idArticle, Integer percentage) {
        this.idArticle = idArticle;
        this.percentage = percentage;
    }

    public String getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(String idArticle) {
        this.idArticle = idArticle;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "ArticleLine{" +
                "idArticle='" + idArticle + '\'' +
                ", percentage=" + percentage +
                '}';
    }
}
