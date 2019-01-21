package es.upm.miw.documents;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;

@Document(collection = "articlesFamily")
public class FamilyArticle extends ArticlesFamily {

    @DBRef
    private Article article;

    public FamilyArticle() {
    }

    public FamilyArticle(Article article) {
        super(FamilyType.ARTICLE);
        this.article = article;
    }

    @Override
    public String getReference() {
        return this.article.getReference();
    }

    @Override
    public String getDescription() {
        return this.article.getDescription();
    }

    @Override
    public Integer getStock() {
        return this.article.getStock();
    }

    @Override
    public void add(ArticlesFamily familyComponent) {
    }

    @Override
    public void remove(ArticlesFamily familyComponent) {
    }

    @Override
    public List<ArticlesFamily> getArticlesFamilyList() {
        return null;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Override
    public String toString() {
        return "FamilyArticle [" + super.toString() + "article=" + article + "]";
    }

    @Override
    public List<String> getArticleIdList() {
        return Arrays.asList(this.article.getCode());
    }

}
