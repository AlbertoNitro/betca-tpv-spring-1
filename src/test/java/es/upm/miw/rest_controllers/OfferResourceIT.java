package es.upm.miw.rest_controllers;

import es.upm.miw.documents.ArticleLine;
import es.upm.miw.documents.Offer;
import es.upm.miw.dtos.input.OfferInputDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
class OfferResourceIT {

    @Autowired
    private RestService restService;

    @BeforeEach
    void seed() {
    }

   @Test
    void testCreateOfferResource() {
       ArticleLine articleLine = new ArticleLine();
       articleLine.setIdArticle("8400000000055");
       articleLine.setPercentage(5);
       ArticleLine[] articleLinesResource = { articleLine };
       OfferInputDto offerInputDto = new OfferInputDto("FakeOfferName Resource", LocalDateTime.now(), articleLinesResource);

        Offer offer = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<Offer>())
                .clazz(Offer.class)
                .path(OfferResource.OFFERS)
                .body(offerInputDto)
                .post()
                .build();
       assertNotNull(offer);
       assertNotNull(offer.getId());
       assertNotNull(offer.getOffername());
       assertNotNull(offer.getEndDate());
       assertNotNull(offer.getArticleLine());
       assertEquals(offerInputDto.getOffername(),  offer.getOffername());
    }

    @Test
    void testReadAllOffersResource(){
        List<Offer> offers = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<Offer[]>())
                .clazz(Offer[].class)
                .path(OfferResource.OFFERS)
                .get()
                .build());
        assertNotNull(offers);
        assertTrue(offers.size() > 0);
    }

    @Test
    void testFilterOffersResource(){
        List<Offer> offers = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<Offer[]>())
                .clazz(Offer[].class)
                .path(OfferResource.OFFERS)
                .path(OfferResource.SEARCH)
                .param("id", "aaaa")
                .param("offername", "aaaa")
                .param("idArticle", "aaa")
                .param("status", "false")
                .get()
                .build());
        assertEquals(0,offers.size());
    }

    @Test
    void testDeleteNoExistsOfferResource() {
        this.restService.loginAdmin().restBuilder(new RestBuilder<Offer>())
                .clazz(Offer.class).path(OfferResource.OFFERS)
                .path(OfferResource.OFFER_ID)
                .expand("aaaa")
                .delete().build();
    }
}
