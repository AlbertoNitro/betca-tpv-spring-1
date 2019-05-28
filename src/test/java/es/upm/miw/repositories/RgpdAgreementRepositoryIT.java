package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.RgpdAgreement;
import es.upm.miw.documents.RgpdAgreementType;
import es.upm.miw.documents.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class RgpdAgreementRepositoryIT {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RgpdAgreementRepository rgpdAgreementRepository;

    private User user1;
    private User user2;
    private User user3;

    private RgpdAgreement agreement1;
    private RgpdAgreement agreement2;
    private RgpdAgreement agreement3;

    @BeforeEach
    void seedDb() {
        this.user1 = new User("666001987", "666001987", "666001987");
        this.userRepository.save(this.user1);
        this.agreement1 = new RgpdAgreement(RgpdAgreementType.BASIC, null, this.user1);
        this.rgpdAgreementRepository.save(this.agreement1);

        this.user2 = new User("123445", "123445", "666001110", "123445", "10", "C/ TPV, 100, 1A, 28000 Madrid", "user2@gmail.com");
        this.userRepository.save(this.user2);
        this.agreement2 = new RgpdAgreement(RgpdAgreementType.MEDIUM, null, this.user2);
        this.rgpdAgreementRepository.save(this.agreement2);

        this.user3 = new User("1234457", "1234457", "666001111", "1234457", "20", "C/ TPV, 100, 1A, 28000 Madrid", "user3@gmail.com");
        this.userRepository.save(user3);
        this.agreement3 = new RgpdAgreement(RgpdAgreementType.ADVANCE, null, this.user3);
        this.rgpdAgreementRepository.save(this.agreement3);
    }

    @Test
    void testfindByAssignee() {
        List<RgpdAgreement> rgpdAgreementList = this.rgpdAgreementRepository.findByAssignee(this.user1.getId());
        assertEquals(1, rgpdAgreementList.size());
        RgpdAgreement rgpdAgreement = rgpdAgreementList.get(0);
        assertEquals(this.agreement1.getType(), rgpdAgreement.getType());
        assertEquals(this.agreement1.getAgreement(), rgpdAgreement.getAgreement());
        assertEquals(this.agreement1.getAssignee().getId(), rgpdAgreement.getAssignee().getId());
    }

    @Test
    void testDeleteAssignee() {
        this.rgpdAgreementRepository.delete(this.agreement2);
        List<RgpdAgreement> rgpdAgreementList = this.rgpdAgreementRepository.findByAssignee(this.user2.getId());
        assertEquals(0, rgpdAgreementList.size());
    }

    @Test
    void testSaveAssignee() {
        List<RgpdAgreement> rgpdAgreementList = this.rgpdAgreementRepository.findByAssignee(this.user3.getId());
        assertEquals(1, rgpdAgreementList.size());
        RgpdAgreement rgpdAgreement = rgpdAgreementList.get(0);
        assertEquals(this.agreement3.getType(), rgpdAgreement.getType());
        assertNotEquals(RgpdAgreementType.BASIC, this.agreement3);
        this.agreement3.setType(RgpdAgreementType.BASIC);
        this.rgpdAgreementRepository.save(this.agreement3);
        rgpdAgreementList = this.rgpdAgreementRepository.findByAssignee(this.user3.getId());
        assertEquals(1, rgpdAgreementList.size());
        rgpdAgreement = rgpdAgreementList.get(0);
        assertEquals(RgpdAgreementType.BASIC, rgpdAgreement.getType());
    }

    @AfterEach
    void delete() {
        this.userRepository.delete(this.user1);
        this.userRepository.delete(this.user2);
        this.userRepository.delete(this.user3);
        this.rgpdAgreementRepository.delete(this.agreement1);
        this.rgpdAgreementRepository.delete(this.agreement2);
        this.rgpdAgreementRepository.delete(this.agreement3);
    }


}
