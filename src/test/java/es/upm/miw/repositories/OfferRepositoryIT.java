package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.ArticleLine;
import es.upm.miw.documents.Offer;
import es.upm.miw.dtos.output.OfferOutputDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class OfferRepositoryIT {

    @Autowired
    private OfferRepository offerRepository;

    private Offer offer;

    @BeforeEach
    void seedDb() {
        ArticleLine articleLine_1 = new ArticleLine();
        articleLine_1.setIdArticle("8400000000017");
        articleLine_1.setPercentage(5);
        ArticleLine articleLine_2 = new ArticleLine();
        articleLine_2.setIdArticle("8400000000024");
        articleLine_2.setPercentage(8);
        ArticleLine[] articleLines = { articleLine_1, articleLine_2 };

        this.offer = new Offer();
        this.offer.setId("0123456789");
        this.offer.setOffername("FakeOfferName");
        this.offer.setEndDate(LocalDateTime.now());
        this.offer.setArticleLine(articleLines);
        this.offerRepository.save(this.offer);
    }

    @AfterEach
    void delete() {
        this.offerRepository.delete(this.offer);
    }

    @Test
    void testReadAll() {
        assertTrue(this.offerRepository.findAll().size() >= 1);
    }

    @Test
    void testfindByIdOffernameEndDateArticleId(){
        LocalDate now = LocalDate.now();
        Instant instant = now.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date dateNow = Date.from(instant);
        List<OfferOutputDto> offerList = this.offerRepository.findByIdOffernameEndDateArticleId(
                "345", "keOf", "false", dateNow, "024");
        assertTrue( offerList.size() > 0);
    }
}
