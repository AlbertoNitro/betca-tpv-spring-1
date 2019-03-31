package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.business_services.PdfService;
import es.upm.miw.documents.RgpdAgreement;
import es.upm.miw.documents.RgpdAgreementType;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.RgpdDto;
import es.upm.miw.repositories.RgpdAgreementRepository;
import es.upm.miw.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class RgpdControllerIT {

    @Autowired
    private RgpdController rgpdController;
    private User user;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private RgpdAgreementRepository rgpdAgreementRepository;
    private RgpdAgreement rgpdAgreement;

    @BeforeEach
    void seedDb() {
        this.user = new User("123445", "123445", "666001110", "123445", "C/ TPV, 100, 1A, 28000 Madrid", "user2@gmail.com");
        this.userRepository.save(user);
        this.rgpdAgreement = new RgpdAgreement();
        this.rgpdAgreement.setType(RgpdAgreementType.MEDIUM);
        this.rgpdAgreement.setAgreement(pdfService.generatePrintableRgpdAgreement(user, rgpdAgreement.getType()));
        this.rgpdAgreement.setAssignee(this.user);
        this.rgpdAgreementRepository.save(this.rgpdAgreement);
    }

    @AfterEach
    void deleteUserDB() {
        this.userRepository.delete(this.user);
        this.rgpdAgreementRepository.delete(this.rgpdAgreement);
    }

    @Test
    void testCreatePrintableAgreement() {
        RgpdDto dtoInput = new RgpdDto();
        dtoInput.setAgreementType(RgpdAgreementType.MEDIUM.toString());
        RgpdDto results = this.rgpdController.createPrintableAgreement(this.user, RgpdAgreementType.getRgpdAgreementType(dtoInput.getAgreementType()));
        assertEquals(dtoInput.getAgreementType(), results.getAgreementType());
        assertNotNull(results.getPrintableAgreement());
    }

    @Test
    void testGetUserAgreement() {
        RgpdDto results = this.rgpdController.getUserAgreement(user);
        assertEquals(this.rgpdAgreement.getType().toString(), results.getAgreementType());
        assertTrue(results.isAccepted());
        assertNotNull(results.getPrintableAgreement());
    }

    @Test
    void testGetUserAgreementWithNoAgreement() {
        this.deleteUserAgreements(this.user);
        RgpdDto results = this.rgpdController.getUserAgreement(this.user);
        assertNull(results.getPrintableAgreement());
        assertNull(results.getAgreementType());
        assertFalse(results.isAccepted());
    }

    @Test
    void testSaveUserAgreement() {
        this.deleteUserAgreements(this.user);
        RgpdAgreementType typeExpected = RgpdAgreementType.ADVANCE;
        byte[] agreement = pdfService.generatePrintableRgpdAgreement(user, typeExpected);
        RgpdDto results = this.rgpdController.saveUserAgreement(user, typeExpected, agreement);
        assertEquals(typeExpected.toString(), results.getAgreementType());
        assertNotNull(results.getPrintableAgreement());
        assertTrue(results.isAccepted());
    }

    @Test
    void testDeleteUserAgreement() {
        this.rgpdController.deleteUserAgreement(this.user);
        RgpdDto result = this.rgpdController.getUserAgreement(this.user);
        assertFalse(result.isAccepted());
        assertNull(result.getAgreementType());
        assertNull(result.getPrintableAgreement());
    }

    private void deleteUserAgreements(User user) {
        for (RgpdAgreement rgpdAgreement : this.rgpdAgreementRepository.findByAssignee(user.getId()))
            this.rgpdAgreementRepository.delete(rgpdAgreement);
    }
}
