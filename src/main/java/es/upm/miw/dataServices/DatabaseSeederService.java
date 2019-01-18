package es.upm.miw.dataServices;

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
    private String ymlFileName;

    @Autowired
    private UserRepository userRepository;

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
        //TODO createArticleVariousIfNotExist
        //TODO createCashierClosureIfNotExist
    }

    public void deleteAllAndInitialize() {
        LogManager.getLogger(this.getClass()).warn("------- Delete All -----------");
        // Delete Repositories -----------------------------------------------------
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

    private void seedDatabase(String ymlFileName) throws IOException {
        assert ymlFileName != null && !ymlFileName.isEmpty();
        Yaml yamlParser = new Yaml(new Constructor(DatabaseGraph.class));
        InputStream input = new ClassPathResource(ymlFileName).getInputStream();
        DatabaseGraph tpvGraph = yamlParser.load(input);

        // Save Repositories -----------------------------------------------------
        this.userRepository.saveAll(tpvGraph.getUserList());
        // -----------------------------------------------------------------------

        LogManager.getLogger(this.getClass()).warn("------- Seed: " + ymlFileName + "-----------");
    }


}
