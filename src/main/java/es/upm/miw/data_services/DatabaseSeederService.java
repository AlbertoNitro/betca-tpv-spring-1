package es.upm.miw.data_services;

import es.upm.miw.documents.*;
import es.upm.miw.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

@Service
public class DatabaseSeederService {

    private static final String VARIOUS_CODE = "1";

    private static final String VARIOUS_NAME = "Varios";
    @Autowired
    public TicketRepository ticketRepository;
    @Autowired
    public InvoiceRepository invoiceRepository;
    @Autowired
    public CashierClosureRepository cashierClosureRepository;
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

    @PostConstruct
    public void constructor() {
        this.initialize();
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
            cashierClosure = new CashierClosure(BigDecimal.ZERO);
            cashierClosure.close(BigDecimal.ZERO, BigDecimal.ZERO, "Initial");
            this.cashierClosureRepository.save(cashierClosure);
        }
        if (!this.articleRepository.existsById(VARIOUS_CODE)) {
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
        this.tagRepository.deleteAll();

        this.ticketRepository.deleteAll();
        this.orderRepository.deleteAll();
        this.familyArticleRepository.deleteAll();
        this.budgetRepository.deleteAll();

        this.articleRepository.deleteAll();

        this.voucherRepository.deleteAll();
        this.cashierClosureRepository.deleteAll();
        this.providerRepository.deleteAll();
        this.userRepository.deleteAll();

        // -------------------------------------------------------------------------

        this.initialize();
    }

    public void deleteAllAndInitializeAndLoadYml() {
        this.deleteAllAndInitialize();
        if (ymlFileName != null) {
            try {
                this.seedDatabase(ymlFileName);
            } catch (IOException e) {
                LogManager.getLogger(this.getClass()).error("File " + ymlFileName + " doesn't exist or can't be opened");
            }
        } else {
            LogManager.getLogger(this.getClass()).error("File " + ymlFileName + " doesn't configured");
        }
        this.initialize();
    }

    public void seedDatabase(String ymlFileName) throws IOException {
        Yaml yamlParser = new Yaml(new Constructor(DatabaseGraph.class));
        InputStream input = new ClassPathResource(ymlFileName).getInputStream();
        DatabaseGraph tpvGraph = yamlParser.load(input);

        // Save Repositories -----------------------------------------------------
        this.userRepository.saveAll(tpvGraph.getUserList());
        this.voucherRepository.saveAll(tpvGraph.getVoucherList());
        this.cashierClosureRepository.saveAll(tpvGraph.getCashierClosureList());
        this.providerRepository.saveAll(tpvGraph.getProviderList());
        this.articleRepository.saveAll(tpvGraph.getArticleList());
        this.ticketRepository.saveAll(tpvGraph.getTicketList());
        this.invoiceRepository.saveAll(tpvGraph.getInvoiceList());
        // -----------------------------------------------------------------------

        LogManager.getLogger(this.getClass()).warn("------- Seed: " + ymlFileName + "-----------");
    }

}
