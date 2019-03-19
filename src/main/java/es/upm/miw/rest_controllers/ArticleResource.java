package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.ArticleController;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import es.upm.miw.dtos.ArticleSearchDto;
import es.upm.miw.dtos.input.FamilySizeInputDto;
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

    public static final String QUERY = "/query";

    public static final String QUERY2 = "/query2";

    public static final String QUERY3 = "/query3";

    public static final String QUERY4 = "/query4";

    public static final String QUERY5 = "/query5";

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

    @GetMapping(value = QUERY)
    public List<ArticleSearchDto> readArticles(@RequestBody String description) {
        return this.articleController.readArticles(description);
    }

    @GetMapping(value = QUERY2)
    public List<ArticleSearchDto> readArticles(@RequestBody int stock) {
        return this.articleController.readArticles(stock);
    }

    @GetMapping(value = QUERY3)
    public List<ArticleSearchDto> readArticlesMinPrice(@RequestBody BigDecimal minPrice) {
        return this.articleController.readArticlesMinPrice(minPrice);
    }

    @GetMapping(value = QUERY4)
    public List<ArticleSearchDto> readArticlesMaxPrice(@RequestBody BigDecimal maxPrice) {
        return this.articleController.readArticlesMaxPrice(maxPrice);
    }

    @GetMapping(value = QUERY5)
    public List<ArticleSearchDto> readArticles() {
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
}
