package es.upm.miw.business_services;

import es.upm.miw.rest_controllers.ApiTestConfig;
import org.springframework.beans.factory.annotation.Autowired;

@ApiTestConfig
public class EmailServiceImplIT {

    @Autowired
    EmailServiceImpl emailService;
/*  @Test
    void testSimpleMessage() {
        emailService.sendSimpleMessage("miguelcalderon10@gmail.com", "Stock from article", "New stock from article");
    }   */
}
