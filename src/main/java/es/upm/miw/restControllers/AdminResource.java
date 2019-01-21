package es.upm.miw.restControllers;

import es.upm.miw.businessControllers.AdminController;
import es.upm.miw.exceptions.FileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping(AdminResource.ADMINS)
public class AdminResource {

    public static final String ADMINS = "/admins";

    public static final String DB = "/db";

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @Autowired
    private AdminController adminController;

    @DeleteMapping(value = DB)
    public void deleteDb() {
        this.adminController.deleteDb();
    }

    @PostMapping(value = DB)
    public void seedDb(@RequestBody String ymlFileName) throws FileException {
        this.adminController.seedDatabase(ymlFileName);
    }

}
