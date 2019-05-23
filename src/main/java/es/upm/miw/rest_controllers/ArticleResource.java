package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.ArticleController;
import es.upm.miw.business_controllers.StockPredictionController;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Provider;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import es.upm.miw.dtos.input.ArticleSearchInputDto;
import es.upm.miw.dtos.input.FamilySizeInputDto;
import es.upm.miw.dtos.input.PeriodicityType;
import es.upm.miw.dtos.input.StockPredictionInputDto;
import es.upm.miw.dtos.output.ArticleSearchOutputDto;
import es.upm.miw.dtos.output.StockPredictionOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public static final String PARTIALLY_DEFINED = "/partially-defined";
    static final String STOCK_PREDICTION = "/stock-prediction";
    public static final String PROVIDER = "/provider";

    @Autowired
    private ArticleController articleController;
    @Autowired
    private StockPredictionController stockPredictionController;

    @GetMapping
    public List<ArticleSearchOutputDto> readAll() {
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

    @PostMapping(value = SEARCH)
    public List<ArticleSearchOutputDto> readArticles(@RequestBody ArticleSearchInputDto article) {
        return this.articleController.readArticles(article.getDescription(), article.getStock(),
                article.getMinPrice(), article.getMaxPrice());
    }

    @PostMapping(value = SEARCH + PARTIALLY_DEFINED)
    public List<ArticleSearchOutputDto> readArticles() {
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
        return this.stockPredictionController.calculateStockPrediction(input);
    }

    @PostMapping(value = PROVIDER)
    public List<Article> findByProvider(@Valid @RequestBody Provider provider) {
        System.out.println( "Provider: " + provider);
        System.out.println( "FindByProvider: " + this.articleController.findArticleByprovider(provider.getId()));
        return this.articleController.findArticleByprovider(provider.getId());
    }
}
