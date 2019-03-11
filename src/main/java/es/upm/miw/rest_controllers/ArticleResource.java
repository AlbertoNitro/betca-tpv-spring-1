package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.ArticleController;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.FamilySizeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(ArticleResource.ARTICLES)
public class ArticleResource {

    public static final String ARTICLES = "/articles";

    public static final String CODE_ID = "/{code}";

    public static final String FAMILY_SIZE = "/family-size";

    @Autowired
    private ArticleController articleController;

    @GetMapping(value = CODE_ID)
    public ArticleDto readArticle(@PathVariable String code) {
        return this.articleController.readArticle(code);
    }

    @PostMapping
    public ArticleDto createArticle(@Valid @RequestBody ArticleDto articleDto) {
        return this.articleController.createArticle(articleDto);
    }

    @PostMapping(value = FAMILY_SIZE)
    public FamilySizeDto createFamilySize(@Valid @RequestBody FamilySizeDto familySizeDto) {
        // TODO: Business Controller
        return familySizeDto;
    }


}
