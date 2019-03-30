package es.upm.miw.dtos.input;


import es.upm.miw.documents.ArticleLine;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;

public class OfferInputDto {

    @NotNull
    private String offername;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private ArticleLine[] articleLine;

    public OfferInputDto(String offername, LocalDateTime endDate, ArticleLine[] articleLine) {
        this.offername = offername;
        this.endDate = endDate;
        this.articleLine = articleLine;
    }

    public String getOffername() {
        return offername;
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

    @Override
    public String toString() {
        return "OfferInputDto{" +
                "offername='" + offername + '\'' +
                ", endDate=" + endDate +
                ", articleLine=" + Arrays.toString(articleLine) +
                '}';
    }
}