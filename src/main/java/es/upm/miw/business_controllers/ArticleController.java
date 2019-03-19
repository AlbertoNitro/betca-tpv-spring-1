package es.upm.miw.business_controllers;

import es.upm.miw.data_services.DatabaseSeederService;
import es.upm.miw.documents.*;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import es.upm.miw.dtos.ArticleSearchDto;
import es.upm.miw.dtos.input.FamilySizeInputDto;
import es.upm.miw.exceptions.ConflictException;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.FamilyArticleRepository;
import es.upm.miw.repositories.FamilyCompositeRepository;
import es.upm.miw.repositories.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private DatabaseSeederService databaseSeederService;

    @Autowired
    private FamilyArticleRepository familyArticleRepository;

    @Autowired
    private FamilyCompositeRepository familyCompositeRepository;

    public List<ArticleSearchDto> readAll() {
        List<ArticleSearchDto> articleSearchDtoList = new ArrayList<>();

        for (Article article : this.articleRepository.findAll()) {
            articleSearchDtoList.add(new ArticleSearchDto(article));
        }

        return articleSearchDtoList;
    }

    public List<ArticleMinimumDto> readArticlesMinimum() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleMinimumDto> dtos = new ArrayList<>();
        for (Article article : articles) {
            dtos.add(new ArticleMinimumDto(article));
        }
        return dtos;
    }

    public ArticleDto readArticle(String code) {
        return new ArticleDto(this.articleRepository.findById(code)
                .orElseThrow(() -> new NotFoundException("Article code (" + code + ")")));
    }

    public List<ArticleSearchDto> readArticles(String description) {
        return this.articleRepository.findByDescriptionLikeIgnoreCaseNullSafe(description);
    }

    public List<ArticleSearchDto> readArticles(int stock) {
        return this.articleRepository.findByStockGreaterThanEqual(stock);
    }

    public List<ArticleSearchDto> readArticlesMinPrice(BigDecimal minPrice) {
        return this.articleRepository.findByRetailPriceGreaterThanEqual(minPrice);
    }

    public List<ArticleSearchDto> readArticlesMaxPrice(BigDecimal maxPrice) {
        return this.articleRepository.findByRetailPriceLessThanEqual(maxPrice);
    }

    public List<ArticleSearchDto> readArticles() {
        return this.articleRepository.findByReferenceNullAndProviderNull();
    }

    public ArticleDto createArticle(ArticleDto articleDto) {
        String code = articleDto.getCode();
        if (code == null) {
            code = this.databaseSeederService.nextCodeEan();
        }

        Article article = prepareArticle(articleDto, code);

        this.articleRepository.save(article);
        return new ArticleDto(article);
    }

    public ArticleDto update(String code, ArticleDto articleDto) {
        Article article = prepareArticle(articleDto, code);

        this.articleRepository.save(article);
        return new ArticleDto(article);
    }

    private Article prepareArticle(ArticleDto articleDto, String code) {
        if (this.articleRepository.findById(code).isPresent()) {
            throw new ConflictException("Article code (" + code + ")");
        }
        int stock = (articleDto.getStock() == null) ? 10 : articleDto.getStock();
        Provider provider = null;
        if (articleDto.getProvider() != null) {
            provider = this.providerRepository.findById(articleDto.getProvider())
                    .orElseThrow(() -> new NotFoundException("Provider (" + articleDto.getProvider() + ")"));
        }

        return Article.builder(code).description(articleDto.getDescription()).retailPrice(articleDto.getRetailPrice())
                .reference(articleDto.getReference()).stock(stock).provider(provider).build();
    }

    public void delete(String code) {
        Optional<Article> article = this.articleRepository.findById(code);

        if (article.isPresent()) {
            this.articleRepository.delete(article.get());
        }

    }

    public FamilySizeInputDto createFamilySize(FamilySizeInputDto familySizeInputDto) {
        String reference = familySizeInputDto.getReference();
        String description = familySizeInputDto.getDescription();
        Provider provider = new Provider(familySizeInputDto.getProvider());
        ArrayList<String> sizesArray = familySizeInputDto.getSizesArray();

        FamilyComposite familyComposite = new FamilyComposite(FamilyType.SIZES, reference, description);
        sizesArray.forEach(size -> {
            Article article = Article.builder(this.databaseSeederService.nextCodeEan())
                    .reference(reference + " T-" + size)
                    .description(description + " T-" + size)
                    .provider(provider)
                    .build();
            FamilyArticle familyArticle = new FamilyArticle(article);
            this.familyArticleRepository.save(familyArticle);
            familyComposite.add(familyArticle);
        });
        this.familyCompositeRepository.save(familyComposite);

        return new FamilySizeInputDto(familyComposite);
    }

}
