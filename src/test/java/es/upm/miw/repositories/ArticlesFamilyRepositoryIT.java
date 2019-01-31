package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class ArticlesFamilyRepositoryIT {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticlesFamilyRepository articlesFamilyRepository;

    @Autowired
    private FamilyArticleRepository familyArticleRepository;

    @Autowired
    private FamilyCompositeRepository familyCompositeRepository;

    @Test
    void testReadAll() {
        ArticlesFamily articleFamily1 = new FamilyArticle(this.articleRepository.findById("8400000000017").get());
        ArticlesFamily articleFamily2 = new FamilyArticle(this.articleRepository.findById("8400000000024").get());
        this.articlesFamilyRepository.save(articleFamily1);
        this.articlesFamilyRepository.save(articleFamily2);

        ArticlesFamily sizes = new FamilyComposite(FamilyType.SIZES, "Zz Falda", "Zarzuela - Falda");
        sizes.add(articleFamily1);
        sizes.add(articleFamily2);
        this.articlesFamilyRepository.save(sizes);

        ArticlesFamily family = new FamilyComposite(FamilyType.ARTICLES, "Zz", "Zarzuela");
        family.add(sizes);
        this.articlesFamilyRepository.save(family);

        assertTrue(this.articlesFamilyRepository.findById(family.getId()).isPresent());
        assertTrue(this.articlesFamilyRepository.findById(sizes.getId()).isPresent());
        assertTrue(this.articlesFamilyRepository.findById(articleFamily1.getId()).isPresent());
        assertTrue(this.articlesFamilyRepository.findById(articleFamily2.getId()).isPresent());

        assertTrue(this.articlesFamilyRepository.findById(family.getId()).get().getArticleIdList()
                .containsAll(Arrays.asList("8400000000017","8400000000024")));

        this.familyCompositeRepository.deleteAll();
        this.familyArticleRepository.deleteAll();

    }

}
