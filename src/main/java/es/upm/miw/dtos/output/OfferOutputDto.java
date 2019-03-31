package es.upm.miw.dtos.output;

import es.upm.miw.documents.ArticleLine;
import es.upm.miw.dtos.input.OfferInputDto;

import java.time.LocalDateTime;
import java.util.Arrays;

public class OfferOutputDto extends OfferInputDto {

    private String id;

    public OfferOutputDto(String id, String offername, LocalDateTime endDate, ArticleLine[] articleLine) {
        super(offername, endDate, articleLine);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "OfferOutputDto{" +
                "id='" + id + '\'' +
                ", offername='" + super.getOffername() + '\'' +
                ", endDate='" + super.getEndDate() + '\'' +
                ", articleLine='" + Arrays.toString(super.getArticleLine()) + '\'' +
                '}';
    }
}