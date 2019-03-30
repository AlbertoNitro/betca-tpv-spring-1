package es.upm.miw.rest_controllers;

import es.upm.miw.documents.ArticleLine;
import es.upm.miw.dtos.input.OfferInputDto;
import es.upm.miw.dtos.output.OfferOutputDto;
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

   /*@Test
    void testCreateOfferResource() {
       ArticleLine articleLine = new ArticleLine();
       articleLine.setIdArticle("8400000000055");
       articleLine.setPercentage(5);
       ArticleLine[] articleLinesResource = { articleLine };
       OfferInputDto offerInputDto = new OfferInputDto("FakeOfferName", LocalDateTime.now(), articleLinesResource);

        OfferOutputDto offerOutputDto = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<OfferOutputDto>())
                .clazz(OfferOutputDto.class)
                .path(OfferResource.OFFERS)
                .body(offerInputDto)
                .post()
                .build();
        assertNotNull(offerOutputDto);
        assertNotNull(offerOutputDto.getId());
    }

    @Test
    void testReadAllOffersResource(){
        List<OfferOutputDto> offers = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<OfferOutputDto[]>())
                .clazz(OfferOutputDto[].class)
                .path(OfferResource.OFFERS)
                .get()
                .build());
        assertNotNull(offers);
        assertTrue(offers.size() > 0);
    }*/
}
