package es.upm.miw.businessControllers;

import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    public ArticleDto readArticle(String code) throws NotFoundException {
        return new ArticleDto(this.articleRepository.findById(code)
                .orElseThrow(() -> new NotFoundException("Article code (" + code + ")")));
    }

}
