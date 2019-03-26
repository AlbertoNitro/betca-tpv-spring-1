package es.upm.miw.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class Alarm {

    @Id
    private String code;

    @DBRef
    private Article article;

    private int warning;

    private int critical;

    public Alarm() {}

    public String getCode() { return code; }

    public void setCode(String code) {
        this.code = code;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public int getWarning() {
        return warning;
    }

    public void setWarning(int warning) {
        this.warning = warning;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alarm alarm = (Alarm) o;
        return Objects.equals(code, alarm.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "code='" + code + '\'' +
                ", article=" + article +
                ", warning=" + warning +
                ", critical=" + critical +
                '}';
    }
}
