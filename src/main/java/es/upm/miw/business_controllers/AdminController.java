package es.upm.miw.business_controllers;

import es.upm.miw.data_services.DatabaseSeederService;
import es.upm.miw.exceptions.FileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class AdminController {

    @Autowired
    private DatabaseSeederService databaseSeederService;

    public void deleteDb() {
        this.databaseSeederService.deleteAllAndInitialize();
    }

    public void seedDatabase(String ymlFileName) throws FileException {
        try {
            this.databaseSeederService.seedDatabase(ymlFileName);
        } catch (IOException e) {
            throw new FileException(e.getMessage());
        }
    }

}
