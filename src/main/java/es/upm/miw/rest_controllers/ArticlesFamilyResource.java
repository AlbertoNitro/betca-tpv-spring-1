package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.ArticlesFamilyController;
import es.upm.miw.documents.FamilyType;
import es.upm.miw.dtos.ArticleFamilyDto;
import es.upm.miw.dtos.ArticleFamilyMinimumDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(ArticlesFamilyResource.ARTICLES_FAMILY)
public class ArticlesFamilyResource {

    public static final String ARTICLE = "/article";

    public static final String ARTICLES_FAMILY = "/articles-family";

    public static final String COMPOSITE = "/composite";

    public static final String DESCRIPTION = "/{description}";

    @Autowired
    private ArticlesFamilyController articlesFamilyController;

    @PostMapping(value = ARTICLE)
    public ArticleMinimumDto createFamilyArticle(@Valid @RequestBody ArticleMinimumDto articleMinimumDto,
                                                 @RequestParam String description) {
        return articlesFamilyController.createFamilyArticle(articleMinimumDto, description);
    }

    @PostMapping(value = COMPOSITE)
    public ArticleFamilyDto createFamilyComposite(@Valid @RequestBody ArticleFamilyDto articleFamilyDto,
                                                  @RequestParam String description) {
        return articlesFamilyController.createFamilyComposite(articleFamilyDto, description);
    }

    @DeleteMapping(value = DESCRIPTION)
    public void deleteComponentFromFamily(@PathVariable String description, @RequestParam String childDescription) {
        articlesFamilyController.deleteComponentFromFamily(description, childDescription);
    }

    @DeleteMapping
    public void deleteFamilyCompositeItem(@Valid @RequestParam String description) {
        articlesFamilyController.deleteFamilyCompositeItem(description);
    }

    @GetMapping(value = DESCRIPTION)
    public List<ArticleFamilyDto> readAllComponentsInAFamily(@PathVariable String description) {
        return articlesFamilyController.readAllComponentsInAFamily(description);
    }

    @GetMapping
    public List<ArticleFamilyMinimumDto> readAllFamilyCompositeByFamilyType(@Valid @RequestParam FamilyType familyType) {
        return articlesFamilyController.readAllFamilyCompositeByFamilyType(familyType);
    }
}
