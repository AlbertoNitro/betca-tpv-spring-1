package es.upm.miw.rest_controllers;

import es.upm.miw.business_services.PdfService;
import es.upm.miw.documents.RgpdAgreement;
import es.upm.miw.documents.RgpdAgreementType;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.RgpdDto;
import es.upm.miw.repositories.RgpdAgreementRepository;
import es.upm.miw.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
public class RgpdResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RgpdAgreementRepository rgpdAgreementRepository;
    private RgpdAgreement rgpdAgreement;

    @BeforeEach
    void before() {
        this.rgpdAgreement = new RgpdAgreement();
        this.rgpdAgreement.setType(RgpdAgreementType.MEDIUM);
        Optional<User> userOptional = userRepository.findByMobile(restService.loginAdmin().getAdminMobile());
        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        this.rgpdAgreement.setAgreement(pdfService.generatePrintableRgpdAgreement(user.getUsername()));
        this.rgpdAgreement.setAssignee(user);
        this.rgpdAgreementRepository.save(this.rgpdAgreement);
    }

    @AfterEach
    void deleteUserDB() {
        this.rgpdAgreementRepository.delete(this.rgpdAgreement);
    }

    @Test
    void testCreatePrintableAgreement() {
        RgpdDto dtoInput = new RgpdDto();
        dtoInput.setAgreementType("2");
        RgpdDto results = this.restService.loginAdmin().
                restBuilder(new RestBuilder<RgpdDto>()).clazz(RgpdDto.class).
                path(RgpdResource.RGPD)
                .path(RgpdResource.PRINTABLE_AGREEMENT).body(dtoInput).post().build();
        assertEquals(dtoInput.getAgreementType(), results.getAgreementType());
        assertNotNull(results.getPrintableAgreement());
    }

    @Test
    void testCreatePrintableAgreementWithNoAuthenticatedUser() {
        RgpdDto dtoInput = new RgpdDto();
        dtoInput.setAgreementType("2");
        this.restService.logout();
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.restBuilder(new RestBuilder<RgpdDto>()).clazz(RgpdDto.class).
                        path(RgpdResource.RGPD).path(RgpdResource.PRINTABLE_AGREEMENT).
                        body(dtoInput).post().build());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void testGetUserAgreement() {
        RgpdDto results = this.restService.loginAdmin().
                restBuilder(new RestBuilder<RgpdDto>()).clazz(RgpdDto.class).
                path(RgpdResource.RGPD)
                .path(RgpdResource.USER_AGREEMENT).get().build();
        assertEquals("2", results.getAgreementType());
        assertTrue(results.isAccepted());
    }

    @Test
    void testGetUserAgreementWithNoAgreement() {
        this.rgpdAgreementRepository.delete(this.rgpdAgreement);
        RgpdDto results = this.restService.loginAdmin().
                restBuilder(new RestBuilder<RgpdDto>()).clazz(RgpdDto.class).
                path(RgpdResource.RGPD)
                .path(RgpdResource.USER_AGREEMENT).get().build();
        assertNull(results.getAgreementType());
        assertNull(results.getPrintableAgreement());
        assertFalse(results.isAccepted());
    }
}
