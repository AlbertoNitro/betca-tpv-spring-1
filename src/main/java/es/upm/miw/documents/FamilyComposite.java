package es.upm.miw.documents;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "articlesFamily")
public class FamilyComposite extends ArticlesFamily {

    private String reference;

    private String description;

    @DBRef(lazy = true)
    private List<ArticlesFamily> ArticlesfamilyList;

    public FamilyComposite() {
        super(FamilyType.ARTICLES);
        this.ArticlesfamilyList = new ArrayList<>();
    }

    public FamilyComposite(FamilyType familyType, String reference, String description) {
        super(familyType);
        this.reference = reference;
        this.description = description;
        this.ArticlesfamilyList = new ArrayList<>();
    }

    @Override
    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Integer getStock() {
        return null;
    }

    @Override
    public void add(ArticlesFamily articlesFamilyList) {
        assert articlesFamilyList != null;
        this.ArticlesfamilyList.add(articlesFamilyList);
    }

    @Override
    public void remove(ArticlesFamily articlesFamilyList) {
        assert articlesFamilyList != null;
        this.ArticlesfamilyList.remove(articlesFamilyList);
    }

    public List<ArticlesFamily> getFamilyCompositeList() {
        return ArticlesfamilyList;
    }

    public void setFamilyCompositeList(List<ArticlesFamily> familyCompositeList) {
        this.ArticlesfamilyList = familyCompositeList;
    }

    @Override
    public List<ArticlesFamily> getArticlesFamilyList() {
        return this.ArticlesfamilyList;
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();
        for (ArticlesFamily item : ArticlesfamilyList) {
            list.add("DBref:" + item.getId());
        }
        return "FamilyComposite [" + super.toString() + " reference=" + reference + ", description=" + description
                + ", articlesFamilyList=" + list + "]";
    }

    @Override
    public List<String> getArticleIdList() {
        List<String> articleIdList = new ArrayList<>();
        for (ArticlesFamily articlesFamily : this.ArticlesfamilyList) {
            articleIdList.addAll(articlesFamily.getArticleIdList());
        }
        return articleIdList;
    }

}
