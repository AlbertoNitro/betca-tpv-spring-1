package es.upm.miw.business_controllers;

import es.upm.miw.documents.FamilyArticle;
import es.upm.miw.documents.FamilyComposite;
import es.upm.miw.documents.FamilyType;
import es.upm.miw.dtos.ArticleFamilyMinimumDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import es.upm.miw.dtos.FamilyCompositeDto;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.FamilyArticleRepository;
import es.upm.miw.repositories.FamilyCompositeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ArticlesFamilyController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FamilyArticleRepository familyArticleRepository;

    @Autowired
    private FamilyCompositeRepository familyCompositeRepository;

    public ArticleMinimumDto createFamilyArticle(ArticleMinimumDto articleMinimumDto, String description) {
        FamilyComposite familyToBeAttached = this.existFamily(description);
        FamilyArticle familyArticleCreated = this.familyArticleRepository.save(new FamilyArticle(
                articleRepository.findByCode(articleMinimumDto.getCode())));
        familyToBeAttached.getFamilyCompositeList().add(familyArticleCreated);
        familyCompositeRepository.save(familyToBeAttached);
        return articleMinimumDto;
    }

    public FamilyCompositeDto createFamilyComposite(FamilyCompositeDto familyCompositeDto, String description) {
        FamilyComposite familyToBeAttached = this.existFamily(description);
        FamilyComposite compositeCreated = familyCompositeRepository.save(new FamilyComposite(
                familyCompositeDto.getFamilyType(),
                familyCompositeDto.getReference(),
                familyCompositeDto.getDescription()));
        familyToBeAttached.getFamilyCompositeList().add(compositeCreated);
        familyCompositeRepository.save(familyToBeAttached);
        return familyCompositeDto;
    }

    public void deleteFamilyCompositeItem(String description) {
        familyCompositeRepository.delete(familyCompositeRepository.findByDescription(description));
    }

    private FamilyComposite existFamily(String description) {
        FamilyComposite familyToBeAttached = familyCompositeRepository.findByDescription(description);
        if (familyToBeAttached == null) {
            throw new BadRequestException("No valid description provided");
        }
        return familyToBeAttached;
    }

    public List<ArticleFamilyMinimumDto> readAllFamilyCompositeByFamilyType(FamilyType familyType) {
        return familyCompositeRepository.findAllFamilyCompositeByFamilyType(familyType);
    }
}
