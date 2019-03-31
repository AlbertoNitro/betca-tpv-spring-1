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

import java.util.Base64;
import java.util.Iterator;
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
        this.rgpdAgreement.setAgreement(pdfService.generatePrintableRgpdAgreement(user, rgpdAgreement.getType()));
        this.rgpdAgreement.setAssignee(user);
        this.rgpdAgreementRepository.save(this.rgpdAgreement);
    }

    @AfterEach
    void deleteRgpdAgreementDB() {
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
        assertNull(results.getPrintableAgreement());
        assertFalse(results.isAccepted());
    }

    @Test
    void testSaveUserAgreement() {
        User user = getUser(this.restService.loginAdmin().getAdminMobile());
        deleteUserAgreement(user);
        RgpdDto dtoInput = new RgpdDto();
        dtoInput.setAgreementType("2");
        byte[] agreement = pdfService.generatePrintableRgpdAgreement(user, RgpdAgreementType.MEDIUM);
        dtoInput.setPrintableAgreement(Base64.getEncoder().encodeToString(agreement));
        RgpdDto results = this.restService.loginAdmin().
                restBuilder(new RestBuilder<RgpdDto>()).clazz(RgpdDto.class).
                path(RgpdResource.RGPD)
                .path(RgpdResource.USER_AGREEMENT).body(dtoInput).post().build();
        assertEquals(dtoInput.getAgreementType(), results.getAgreementType());
        assertNotNull(results.getPrintableAgreement());
        assertEquals(dtoInput.getPrintableAgreement(), results.getPrintableAgreement());
        deleteUserAgreement(getUser(this.restService.loginAdmin().getAdminMobile()));
    }

    @Test
    void testDeleteUserAgreement() {
        assertDoesNotThrow(() -> this.restService.loginAdmin().
                restBuilder(new RestBuilder<RgpdDto>()).clazz(RgpdDto.class).
                path(RgpdResource.RGPD)
                .path(RgpdResource.USER_AGREEMENT).delete().build());
        RgpdDto rgpd = this.restService.loginAdmin().
                restBuilder(new RestBuilder<RgpdDto>()).clazz(RgpdDto.class).
                path(RgpdResource.RGPD)
                .path(RgpdResource.USER_AGREEMENT).get().build();
        assertFalse(rgpd.isAccepted());
    }

    private void deleteUserAgreement(User user) {
        Iterator<RgpdAgreement> iterator = this.rgpdAgreementRepository.findByAssignee(user.getId()).iterator();
        while (iterator.hasNext())
            this.rgpdAgreementRepository.delete(iterator.next());
    }

    private User getUser(String username) {
        Optional<User> optional = userRepository.findByMobile(username);
        return optional.orElse(null);
    }
}
