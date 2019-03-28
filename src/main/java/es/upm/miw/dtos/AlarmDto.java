package es.upm.miw.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import es.upm.miw.documents.Alarm;
import es.upm.miw.documents.Article;

@JsonInclude(Include.NON_NULL)
public class AlarmDto {

    private Article article;

    private int warning;

    private int critical;

    public AlarmDto(){
        // Empty for framework
    }

    public AlarmDto(Alarm alarm) {
        this.article = alarm.getArticle();
        this.warning = alarm.getWarning();
        this.critical = alarm.getCritical();
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
}
