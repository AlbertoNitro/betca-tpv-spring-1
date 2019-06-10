package es.upm.miw.business_controllers;

import es.upm.miw.data_services.DatabaseSeederService;
import es.upm.miw.documents.*;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.ArticleMinimumDto;
import es.upm.miw.dtos.input.FamilySizeInputDto;
import es.upm.miw.dtos.output.ArticleSearchOutputDto;
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
import java.util.stream.Collectors;

@Controller
public class ArticleController {

    private static final Integer ZERO = 0;
    private static final Integer ONE = 1;

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

    public List<ArticleSearchOutputDto> readAll() {
        return this.articleRepository.findAll().stream().map(ArticleSearchOutputDto::new).collect(Collectors.toList());
    }

    public List<ArticleMinimumDto> readArticlesMinimum() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleMinimumDto> dtos = new ArrayList<>();
        for (Article article : articles) {
            dtos.add(new ArticleMinimumDto(article));
        }
        return dtos;
    }

    public List<ArticleDto> readArticlesMinimumStock(Integer minimumStock){
        List<Article> articles = articleRepository.findByStockBetween(ZERO, minimumStock);
        List<ArticleDto> articleDtos = new ArrayList<>();
        for(Article article : articles){
            articleDtos.add(new ArticleDto(article));
        }
        return articleDtos;
    }

    public List<ArticleDto> readArticlesReservation(){
        List<Article> articles = articleRepository.findByStockLessThan(ONE);
        List<ArticleDto> articleDtos = new ArrayList<>();
        for(Article article : articles){
            articleDtos.add(new ArticleDto(article));
        }
        return articleDtos;
    }

    public ArticleDto readArticle(String code) {
        return new ArticleDto(this.articleRepository.findById(code)
                .orElseThrow(() -> new NotFoundException("Article code (" + code + ")")));
    }

    public List<ArticleSearchOutputDto> readArticles(String description, Integer stock, BigDecimal minPrice, BigDecimal maxPrice) {
        String minPriceStr;
        String maxPriceStr;
        minPriceStr = minPrice == null ? null : minPrice.toString();
        maxPriceStr = maxPrice == null ? null : maxPrice.toString();

        return this.articleRepository.findByDescriptionAndStockAndRetailPriceNullSafe
                (description, stock, minPriceStr, maxPriceStr);
    }

    public List<ArticleSearchOutputDto> readArticles() {
        return this.articleRepository.findByReferenceNullAndProviderNull();
    }

    public ArticleDto createArticle(ArticleDto articleDto) {
        String code = articleDto.getCode();
        if (code == null) {
            code = this.databaseSeederService.nextCodeEan();
        }

        if (this.articleRepository.findById(code).isPresent()) {
            throw new ConflictException("Article code (" + code + ")");
        }

        Article article = prepareArticle(articleDto, code);

        this.articleRepository.save(article);
        return new ArticleDto(article);
    }

    public ArticleDto update(String code, ArticleDto articleDto) {
        ArticleDto articleBBDD = readArticle(code);

        Article article = prepareArticle(articleDto, articleBBDD.getCode());

        this.articleRepository.save(article);
        return new ArticleDto(article);
    }

    private Article prepareArticle(ArticleDto articleDto, String code) {

        int stock = (articleDto.getStock() == null) ? 10 : articleDto.getStock();
        Provider provider = null;
        if (articleDto.getProvider() != null) {
            provider = this.providerRepository.findById(articleDto.getProvider())
                    .orElseThrow(() -> new NotFoundException("Provider (" + articleDto.getProvider() + ")"));
        }

        return Article.builder(code).description(articleDto.getDescription()).retailPrice(articleDto.getRetailPrice())
                .reference(articleDto.getReference()).stock(stock).provider(provider).discontinued(articleDto.getDiscontinued()).build();
    }

    public void delete(String code) {
        if (this.articleRepository.findById(code).isPresent()) {
            this.articleRepository.deleteById(code);
        }
    }

    public FamilySizeInputDto createFamilySize(FamilySizeInputDto familySizeInputDto) {
        String reference = familySizeInputDto.getReference();
        String description = familySizeInputDto.getDescription();
        Provider provider = this.getProvider(familySizeInputDto.getProvider());
        String stock = familySizeInputDto.getStock();
        ArrayList<String> sizesArray = familySizeInputDto.getSizesArray();
        this.createFamilyComposite(reference, description, provider, Integer.parseInt(stock), sizesArray);
        return new FamilySizeInputDto(reference, description, provider.getCompany(), stock, sizesArray);
    }

    public Provider getProvider(String company) {
        Provider provider;
        if(this.providerRepository.findByCompany(company).isPresent()) {
            provider = this.providerRepository.findByCompany(company).get();
        } else {
            provider = new Provider(company);
            this.providerRepository.save(provider);
        }
        return provider;
    }

    public FamilyComposite createFamilyComposite(String reference, String description, Provider provider, Integer stock, List<String> sizesArray) {
        FamilyComposite familyComposite = new FamilyComposite(FamilyType.SIZES, reference, description);

        sizesArray.forEach(size -> {
            Article article = this.createArticleForEachSize(size, reference, description, provider, stock);
            familyComposite.add(this.createFamilyArticle(article));
        });
        this.familyCompositeRepository.save(familyComposite);
        return familyComposite;
    }

    public Article createArticleForEachSize(String size,String reference,String description,Provider provider, Integer stock) {
        Article article = Article.builder(this.databaseSeederService.nextCodeEan())
                .reference(reference + " T-" + size)
                .description(description + " T-" + size)
                .provider(provider)
                .stock(stock)
                .build();
        this.articleRepository.save(article);
        return article;
    }

    public FamilyArticle createFamilyArticle(Article article) {
        FamilyArticle familyArticle = new FamilyArticle(article);
        this.familyArticleRepository.save(familyArticle);
        return familyArticle;
    }

    public List<ArticleSearchOutputDto> findArticleByProvider(String id){
        Optional<Provider> provider = providerRepository.findById(id);
        return (List<ArticleSearchOutputDto>) this.articleRepository.findAllByProvider(provider);
    }
}
