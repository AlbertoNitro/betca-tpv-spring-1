package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.ArticleLine;
import es.upm.miw.documents.Offer;
import es.upm.miw.documents.Tax;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.dtos.input.OfferInputDto;
import es.upm.miw.dtos.output.OfferOutputDto;
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
class OfferControllerIT {

    @Autowired
    private OfferController offerController;

    private OfferOutputDto offerOutputDto;
    private OfferInputDto offerInputDto;

    @BeforeEach
    void seed() {
        ArticleLine articleLine_1 = new ArticleLine();
        articleLine_1.setIdArticle("8400000000017");
        articleLine_1.setPercentage(5);
        ArticleLine articleLine_2 = new ArticleLine();
        articleLine_2.setIdArticle("8400000000024");
        articleLine_2.setPercentage(8);
        ArticleLine[] articleLines = { articleLine_1, articleLine_2 };
        this.offerInputDto = new OfferInputDto("FakeOfferName", LocalDateTime.now(), articleLines);
    }

    @Test
    void testCreateOfferController() {
        this.offerOutputDto = this.offerController.create(this.offerInputDto);
        System.out.println(this.offerOutputDto.toString());
        assertNotNull(this.offerOutputDto.getId());
        assertEquals(this.offerInputDto.getOffername(), offerOutputDto.getOffername());
        assertNotNull(this.offerOutputDto.getEndDate());
        assertNotNull(this.offerOutputDto.getArticleLine());
        assertNotNull(this.offerOutputDto);
    }

    @Test
    void testReadAllOffersController() {
        List<OfferOutputDto> offers = this.offerController.readAll();
        assertNotNull(offers);
        assertTrue( offers.size() >= 1);
    }

    @Test
    void testFilterSearchNullController(){
        List<OfferOutputDto> offerList = this.offerController.search(
                "aaaa", "aaaaa", "", "false");
        assertEquals(0, offerList.size());
    }

    @Test
    void testDeleteOfferNotExistController(){
        this.offerController.delete("aaa");
    }
}
