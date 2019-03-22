package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.ArticleController;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import es.upm.miw.dtos.ArticleSearchDto;
import es.upm.miw.dtos.input.FamilySizeInputDto;
import es.upm.miw.dtos.stock_prediction.PeriodType;
import es.upm.miw.dtos.stock_prediction.PeriodicityType;
import es.upm.miw.dtos.stock_prediction.StockPredictionInputDto;
import es.upm.miw.dtos.stock_prediction.StockPredictionOutputDto;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(ArticleResource.ARTICLES)
public class ArticleResource {

    public static final String ARTICLES = "/articles";
    public static final String CODE_ID = "/{code}";
    public static final String FAMILY_SIZE = "/family-size";
    public static final String MINIMUM = "/minimum";
    public static final String SEARCH = "/search";
    public static final String PARTIALLY_DEFINED = "/partially-prefined";
    static final String STOCK_PREDICTION = "/stock-prediction";

    @Autowired
    private ArticleController articleController;

    @GetMapping
    public List<ArticleSearchDto> readAll() {
        return this.articleController.readAll();
    }

    @GetMapping(value = CODE_ID)
    public ArticleDto readArticle(@PathVariable String code) {
        return this.articleController.readArticle(code);
    }

    @GetMapping(value = MINIMUM)
    public List<ArticleMinimumDto> readArticlesMinimum() {
        return this.articleController.readArticlesMinimum();
    }

    @PostMapping
    public ArticleDto createArticle(@Valid @RequestBody ArticleDto articleDto) {
        return this.articleController.createArticle(articleDto);
    }

    @PostMapping(value = FAMILY_SIZE)
    public FamilySizeInputDto createFamilySize(@Valid @RequestBody FamilySizeInputDto familySizeInputDto) {
        return this.articleController.createFamilySize(familySizeInputDto);
    }

    @GetMapping(value = SEARCH)
    public List<ArticleSearchDto> readArticles(@RequestBody String description, @RequestBody Integer stock,
                                               @RequestBody BigDecimal minPrice, @RequestBody BigDecimal maxPrice){
        return this.articleController.readArticles(description, stock, minPrice, maxPrice);
    }

    @GetMapping(value = SEARCH + PARTIALLY_DEFINED)
    public List<ArticleSearchDto> readArticles(){
        return this.articleController.readArticles();
    }

    @PutMapping(value = CODE_ID)
    public ArticleDto update(@PathVariable String code, @Valid @RequestBody ArticleDto articleDto) {
        return this.articleController.update(code, articleDto);
    }

    @DeleteMapping(value = CODE_ID)
    public void delete(@PathVariable String code) {
        this.articleController.delete(code);
    }

    @GetMapping(value = CODE_ID + STOCK_PREDICTION)
    public StockPredictionOutputDto[] calculateStockPrediction(@PathVariable String code, @RequestParam PeriodicityType periodicityType, @RequestParam int periodsNumber) {
        StockPredictionInputDto input = new StockPredictionInputDto(code, periodicityType, periodsNumber);
        input.validate();

        return new StockPredictionOutputDto[]{
                new StockPredictionOutputDto(PeriodType.WEEK, 1, 1028),
                new StockPredictionOutputDto(PeriodType.WEEK, 2, 964),
                new StockPredictionOutputDto(PeriodType.WEEK, 3, 900),
                new StockPredictionOutputDto(PeriodType.WEEK, 4, 837)
        };
    }


}
