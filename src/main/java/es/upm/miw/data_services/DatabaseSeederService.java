package es.upm.miw.data_services;

import es.upm.miw.business_services.Barcode;
import es.upm.miw.documents.*;
import es.upm.miw.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class DatabaseSeederService {

    private static final String VARIOUS_CODE = "1";

    private static final String VARIOUS_NAME = "Varios";

    private static final String PREFIX_CODE_ARTICLE = "84";

    private static final Long FIRST_CODE_ARTICLE = 840000000000L;

    private static final Long LAST_CODE_ARTICLE = 840000099999L;

    @Autowired
    public TicketRepository ticketRepository;
    @Autowired
    public InvoiceRepository invoiceRepository;
    @Autowired
    public CashierClosureRepository cashierClosureRepository;
    @Autowired
    private Environment environment;

    @Autowired
    private Barcode barcode;

    @Value("${miw.admin.mobile}")
    private String mobile;
    @Value("${miw.admin.username}")
    private String username;
    @Value("${miw.admin.password}")
    private String password;

    @Value("${miw.databaseSeeder.ymlFileName:#{null}}")
    private String ymlFileName;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private ArticlesFamilyRepository articlesFamilyRepository;
    @Autowired
    private FamilyArticleRepository familyArticleRepository;
    @Autowired
    private FamilyCompositeRepository familyCompositeRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TimeClockRepository timeClockRepository;

    @PostConstruct
    public void constructor() {
        String[] profiles = this.environment.getActiveProfiles();
        if (Arrays.stream(profiles).anyMatch("dev"::equals)) {
            this.deleteAllAndInitializeAndLoadYml();
        } else if (Arrays.stream(profiles).anyMatch("prod"::equals)) {
            this.initialize();
        }
    }

    private void initialize() {
        if (!this.userRepository.findByMobile(this.mobile).isPresent()) {
            LogManager.getLogger(this.getClass()).warn("------- Create Admin -----------");
            User user = new User(this.mobile, this.username, this.password);
            user.setRoles(new Role[]{Role.ADMIN});
            this.userRepository.save(user);
        }
        CashierClosure cashierClosure = this.cashierClosureRepository.findFirstByOrderByOpeningDateDesc();
        if (cashierClosure == null) {
            LogManager.getLogger(this.getClass()).warn("------- Create cashierClosure -----------");
            cashierClosure = new CashierClosure(BigDecimal.ZERO);
            cashierClosure.close(BigDecimal.ZERO, BigDecimal.ZERO, "Initial");
            this.cashierClosureRepository.save(cashierClosure);
        }
        if (!this.articleRepository.existsById(VARIOUS_CODE)) {
            LogManager.getLogger(this.getClass()).warn("------- Create Article Various -----------");
            Provider provider = new Provider(VARIOUS_NAME);
            this.providerRepository.save(provider);
            this.articleRepository.save(Article.builder(VARIOUS_CODE).reference(VARIOUS_NAME).description(VARIOUS_NAME)
                    .retailPrice("100.00").stock(1000).provider(provider).build());
        }
    }

    public void deleteAllAndInitialize() {
        LogManager.getLogger(this.getClass()).warn("------- Delete All -----------");
        // Delete Repositories -----------------------------------------------------
        this.familyCompositeRepository.deleteAll();
        this.invoiceRepository.deleteAll();

        this.budgetRepository.deleteAll();
        this.familyArticleRepository.deleteAll();
        this.orderRepository.deleteAll();
        this.tagRepository.deleteAll();
        this.ticketRepository.deleteAll();
        this.timeClockRepository.deleteAll();
        this.articleRepository.deleteAll();

        this.cashierClosureRepository.deleteAll();
        this.providerRepository.deleteAll();
        this.userRepository.deleteAll();
        this.voucherRepository.deleteAll();
        // -------------------------------------------------------------------------
        this.initialize();
    }

    public void deleteAllAndInitializeAndLoadYml() {
        this.deleteAllAndInitialize();
        this.seedDatabase();
        this.initialize();
    }

    public void seedDatabase() {
        if (this.ymlFileName != null) {
            try {
                LogManager.getLogger(this.getClass()).warn("------- Initial Load: " + this.ymlFileName + "-----------");
                this.seedDatabase(new ClassPathResource(this.ymlFileName).getInputStream());
            } catch (IOException e) {
                LogManager.getLogger(this.getClass()).error("File " + this.ymlFileName + " doesn't exist or can't be opened");
            }
        } else {
            LogManager.getLogger(this.getClass()).error("File db.yml doesn't configured");
        }
    }

    private void seedDatabaseWithArticlesFamilyForView() {
        LogManager.getLogger(this.getClass()).warn("------- Create Article Family Root -----------");
        ArticlesFamily root = new FamilyComposite(FamilyType.ARTICLES, "root", "root");

        ArticlesFamily c1 = new FamilyArticle(this.articleRepository.findById("8400000000031").get());
        ArticlesFamily c2 = new FamilyArticle(this.articleRepository.findById("8400000000048").get());
        this.articlesFamilyRepository.save(c1);
        this.articlesFamilyRepository.save(c2);
        ArticlesFamily c3 = new FamilyComposite(FamilyType.ARTICLES, "c", "cards");
        ArticlesFamily c4 = new FamilyComposite(FamilyType.SIZES, null, "X");
        this.articlesFamilyRepository.save(c3);
        this.articlesFamilyRepository.save(c4);

        root.add(c1);
        root.add(c2);
        root.add(c3);
        root.add(c4);
        this.articlesFamilyRepository.save(root);
    }

    public void seedDatabase(InputStream input) {
        Yaml yamlParser = new Yaml(new Constructor(DatabaseGraph.class));
        DatabaseGraph tpvGraph = yamlParser.load(input);

        // Save Repositories -----------------------------------------------------
        this.providerRepository.saveAll(tpvGraph.getProviderList());
        this.userRepository.saveAll(tpvGraph.getUserList());
        this.voucherRepository.saveAll(tpvGraph.getVoucherList());

        this.articleRepository.saveAll(tpvGraph.getArticleList());

        this.budgetRepository.saveAll(tpvGraph.getBudgetList());
        this.familyArticleRepository.saveAll(tpvGraph.getFamilyArticleList());
        this.orderRepository.saveAll(tpvGraph.getOrderList());
        this.tagRepository.saveAll(tpvGraph.getTagList());
        this.ticketRepository.saveAll(tpvGraph.getTicketList());
        this.timeClockRepository.saveAll(tpvGraph.getTimeClockList());

        this.familyCompositeRepository.saveAll(tpvGraph.getFamilyCompositeList());
        this.invoiceRepository.saveAll(tpvGraph.getInvoiceList());
        // -----------------------------------------------------------------------

        // Seed ArticlesFamilyRepository  ----------------------------------------
        this.seedDatabaseWithArticlesFamilyForView();

        LogManager.getLogger(this.getClass()).warn("------- Seed...   " + "-----------");
    }

    public String nextCodeEan() {
        Article article = this.articleRepository.findFirstByCodeStartingWithOrderByRegistrationDateDescCodeDesc(PREFIX_CODE_ARTICLE);

        Long nextCodeWithoutRedundancy = FIRST_CODE_ARTICLE;

        if (article != null) {
            String code = article.getCode();
            String codeWithoutRedundancy = code.substring(0, code.length() - 1);

            nextCodeWithoutRedundancy = Long.parseLong(codeWithoutRedundancy) + 1L;
        }

        if (nextCodeWithoutRedundancy > LAST_CODE_ARTICLE) {
            throw new RuntimeException("There is not next code EAN");
        }

        return this.barcode.generateEan13code(nextCodeWithoutRedundancy);
    }

}





