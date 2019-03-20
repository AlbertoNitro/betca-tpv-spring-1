package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.ArticlesFamilyController;
import es.upm.miw.documents.FamilyType;
import es.upm.miw.dtos.ArticleFamilyMinimumDto;
import es.upm.miw.dtos.FamilyCompositeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(ArticlesFamilyResource.ARTICLES_FAMILY)
public class ArticlesFamilyResource {

    public static final String ARTICLES_FAMILY = "/articles-family";

    public static final String COMPOSITE = "/composite";

    @Autowired
    private ArticlesFamilyController articlesFamilyController;

    @GetMapping
    public List<ArticleFamilyMinimumDto> readAllFamilyCompositeByFamilyType(@Valid @RequestParam FamilyType familyType) {
        return articlesFamilyController.readAllFamilyCompositeByFamilyType(familyType);
    }

    @DeleteMapping
    public void deleteFamilyCompositeItem(@Valid @RequestParam String description) {
        articlesFamilyController.deleteFamilyCompositeItem(description);
    }

    @PostMapping(value = COMPOSITE)
    public FamilyCompositeDto createFamilyComposite(@Valid @RequestBody FamilyCompositeDto familyCompositeDto,
                                                    @RequestParam String description) {
        return articlesFamilyController.createFamilyComposite(familyCompositeDto,description);
    }


}
