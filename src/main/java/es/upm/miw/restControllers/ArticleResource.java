package es.upm.miw.restControllers;

import es.upm.miw.businessControllers.ArticleController;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(ArticleResource.ARTICLES)
public class ArticleResource {

    public static final String ARTICLES = "/articles";

    public static final String CODE_ID = "/{code}";

    @Autowired
    private ArticleController articleController;

    @GetMapping(value = CODE_ID)
    public ArticleDto readArticle(@PathVariable String code) throws NotFoundException {
        return this.articleController.readArticle(code);
    }

}
