package es.upm.miw.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import es.upm.miw.documents.Alarm;
import es.upm.miw.documents.Article;

@JsonInclude(Include.NON_NULL)
public class AlarmDto {

    private String code;

    private String refToArticle;

    private int warning;

    private int critical;

    public AlarmDto(){
        // Empty for framework
    }

    public AlarmDto(Alarm alarm) {
        this.code = alarm.getCode();
        this.refToArticle = alarm.getArticle().getCode();
        this.warning = alarm.getWarning();
        this.critical = alarm.getCritical();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRefToArticle() {
        return refToArticle;
    }

    public void setRefToArticle(String refToArticle) {
        this.refToArticle = refToArticle;
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
    public String toString() {
        return "AlarmDto{" +
                "code='" + code + '\'' +
                ", refArticle='" + refToArticle + '\'' +
                ", warning=" + warning +
                ", critical=" + critical +
                '}';
    }
}
