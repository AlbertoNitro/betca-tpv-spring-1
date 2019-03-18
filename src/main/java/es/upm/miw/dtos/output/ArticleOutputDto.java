package es.upm.miw.dtos.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.documents.Article;
import es.upm.miw.dtos.ArticleMinimumDto;
import es.upm.miw.dtos.validations.BigDecimalPositive;

import java.math.BigDecimal;

public class ArticleOutputDto extends ArticleMinimumDto {

    @BigDecimalPositive
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal retailPrice;

    private Integer stock;

    public ArticleOutputDto() {
        // Empty for framework
    }

    public ArticleOutputDto(String code, String description) {
        super(code, description);
    }

    public ArticleOutputDto(Article article) {
        this(article.getCode(), article.getDescription());
        this.retailPrice = article.getRetailPrice();
        this.stock = article.getStock();
    }
}
