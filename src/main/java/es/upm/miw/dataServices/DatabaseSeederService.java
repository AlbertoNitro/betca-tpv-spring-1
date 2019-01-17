package es.upm.miw.dataServices;

import es.upm.miw.businessServices.Barcode;
import es.upm.miw.documents.Role;
import es.upm.miw.documents.User;
import es.upm.miw.repositories.UserRepository;
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
import java.util.Optional;


@Service
public class DatabaseSeederService {

    private static final String VARIOUS_CODE = "1";

    private static final String VARIOUS_NAME = "Varios";

    @Value("${miw.admin.mobile}")
    private String mobile;
    @Value("${miw.admin.username}")
    private String username;
    @Value("${miw.admin.password}")
    private String password;

    @Value("${miw.databaseSeeder.ymlFileName:#{null}}")
    private Optional<String> ymlFileName;

    @Autowired
    private UserRepository userRepository;

    private long ean13;

    @PostConstruct
    public void constructor() {
        if (this.userRepository.count() == 0) {
            this.createAdminIfNotExist();
        }
        this.initializeEan13();
    }

    private void initializeEan13() {
        this.ean13 = 840000000000L;
        LogManager.getLogger(this.getClass()).warn("Code 84000000* doesn't exist... initialize");
    }

    public void initializeDB() {
        this.createAdminIfNotExist();
        this.createArticleVariousIfNotExist();
        this.createCashierClosureIfNotExist();
    }

    private void createAdminIfNotExist() {
        if (this.userRepository.findByMobile(this.mobile) == null) {
            User user = new User(this.mobile, this.username, this.password);
            user.setRoles(new Role[]{Role.ADMIN});
            this.userRepository.save(user);
        }
    }

    private void createArticleVariousIfNotExist() {
    }

    private void createCashierClosureIfNotExist() {
    }

    public void resetDB() {
        this.deleteAllAndCreateAdmin();
        if (ymlFileName.isPresent()) {
            try {
                this.seedDatabase(ymlFileName.get());
            } catch (IOException e) {
                LogManager.getLogger(this.getClass()).error("File " + ymlFileName + " doesn't exist or can't be opened");
            }
        } else {
            LogManager.getLogger(this.getClass()).error("File " + ymlFileName + " doesn't configured");
        }
        this.initializeDB();
    }

    public String createEan13() {
        this.ean13++;
        return new Barcode().generateEan13code(this.ean13);
    }

    private void seedDatabase(String ymlFileName) throws IOException {
        assert ymlFileName != null && !ymlFileName.isEmpty();
        Yaml yamlParser = new Yaml(new Constructor(DatabaseGraph.class));
        InputStream input = new ClassPathResource(ymlFileName).getInputStream();
        DatabaseGraph tpvGraph = yamlParser.load(input);

        // Save Repositories -----------------------------------------------------
        this.userRepository.saveAll(tpvGraph.getUserList());
        // -----------------------------------------------------------------------

        LogManager.getLogger(this.getClass()).warn("------------------------- Seed: " + ymlFileName + "-----------");
    }


    public void deleteAllAndCreateAdmin() {
        LogManager.getLogger(this.getClass()).warn("------------------------- delete All And Create Admin-----------");
        // Delete Repositories -----------------------------------------------------
        this.userRepository.deleteAll();

        this.initializeDB();
        // -----------------------------------------------------------------------
    }

}
