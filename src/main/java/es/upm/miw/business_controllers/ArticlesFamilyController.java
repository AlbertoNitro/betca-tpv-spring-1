package es.upm.miw.business_controllers;

import es.upm.miw.documents.*;
import es.upm.miw.dtos.ArticleFamilyDto;
import es.upm.miw.dtos.ArticleFamilyMinimumDto;
import es.upm.miw.dtos.ArticleFamilyRootDto;
import es.upm.miw.exceptions.BadRequestException;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.FamilyArticleRepository;
import es.upm.miw.repositories.FamilyCompositeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class ArticlesFamilyController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FamilyArticleRepository familyArticleRepository;

    @Autowired
    private FamilyCompositeRepository familyCompositeRepository;

    public ArticleFamilyDto attachToFamily(ArticleFamilyDto articleFamilyDto, String description) {
        this.existFamily(description);
        FamilyComposite familyToBeAttached = this.existFamily(description);
        if (articleFamilyDto.getFamilyType() == FamilyType.ARTICLE) {
            FamilyArticle familyArticleCreated = this.familyArticleRepository.save(new FamilyArticle(
                    articleRepository.findByCode(articleFamilyDto.getReference())));
            familyToBeAttached.getFamilyCompositeList().add(familyArticleCreated);
            familyCompositeRepository.save(familyToBeAttached);
        } else if (articleFamilyDto.getFamilyType() == FamilyType.ARTICLES || articleFamilyDto.getFamilyType() == FamilyType.SIZES) {
            this.existFamily(articleFamilyDto.getDescription());
            familyToBeAttached.getFamilyCompositeList().add(
                    familyCompositeRepository.findFirstByDescription(articleFamilyDto.getDescription()));
            familyCompositeRepository.save(familyToBeAttached);
        }
        return articleFamilyDto;
    }

    public ArticleFamilyDto createArticleFamily(ArticleFamilyDto articleFamilyDto, String description) {
        this.existFamily(description);
        if (articleFamilyDto.getFamilyType() == FamilyType.ARTICLES || articleFamilyDto.getFamilyType() == FamilyType.SIZES) {
            FamilyComposite compositeCreated = familyCompositeRepository.save(new FamilyComposite(
                    articleFamilyDto.getFamilyType(),
                    articleFamilyDto.getReference(),
                    articleFamilyDto.getDescription()));
            familyCompositeRepository.save(compositeCreated);
        }
        return this.attachToFamily(articleFamilyDto, description);
    }

    public void deleteComponentFromFamily(String description, String childDescription) {
        FamilyComposite family = familyCompositeRepository.findFirstByDescription(description);
        Iterator iterator = family.getArticlesFamilyList().iterator();
        while (iterator.hasNext()) {
            ArticlesFamily component = (ArticlesFamily) iterator.next();
            if (component.getDescription().equals(childDescription))
                iterator.remove();
        }
        familyCompositeRepository.save(family);
    }

    public void deleteFamilyCompositeItem(String description) {
        familyCompositeRepository.delete(familyCompositeRepository.findFirstByDescription(description));
    }

    private FamilyComposite existFamily(String description) {
        FamilyComposite familyToBeAttached = familyCompositeRepository.findFirstByDescription(description);
        if (familyToBeAttached == null) {
            throw new BadRequestException("No valid description provided");
        }
        return familyToBeAttached;
    }

    public List<ArticleFamilyDto> readAllComponentsInAFamily(String description) {
        FamilyComposite family = familyCompositeRepository.findFirstByDescription(description);
        List<ArticleFamilyDto> dtos = new ArrayList<>();
        for (ArticlesFamily articlesFamily : family.getFamilyCompositeList()) {
            dtos.add(new ArticleFamilyDto(articlesFamily));
        }
        return dtos;
    }

    public List<ArticleFamilyMinimumDto> readAllFamilyCompositeByFamilyType(FamilyType familyType) {
        return familyCompositeRepository.findAllFamilyCompositeByFamilyType(familyType);
    }

    public  List<ArticleFamilyRootDto> readInFamilyCompositeArticlesList(String description){
        FamilyComposite familyRoot = familyCompositeRepository.findFirstByDescription(description);
        List<ArticleFamilyRootDto> dtos = new ArrayList<>();
        for (ArticlesFamily articlesFamily: familyRoot.getArticlesFamilyList()) {
            if (articlesFamily.getFamilyType() == FamilyType.ARTICLE){
                Article article = articleRepository.findByCode(articlesFamily.getArticleIdList().get(0));
                dtos.add(new ArticleFamilyRootDto(article.getCode(), article.getDescription(), article.getRetailPrice()));
            }
            if (articlesFamily.getFamilyType() == FamilyType.ARTICLES){
                dtos.add(new ArticleFamilyRootDto(articlesFamily.getDescription(), articlesFamily.getArticlesFamilyList()));
            }
            if (articlesFamily.getFamilyType() == FamilyType.SIZES){
                dtos.add(new ArticleFamilyRootDto(articlesFamily.getReference(), articlesFamily.getDescription()));
            }
        }
        return dtos;
    }
}
