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

    private OfferInputDto offerInputDto;

    @BeforeEach
    void seed() {
        ArticleLine articleLine = new ArticleLine();
        articleLine.setIdArticle("8400000000055");
        articleLine.setPercentage(5);
        ArticleLine[] articleLinesResource = { articleLine };
        this.offerInputDto = new OfferInputDto();
        this.offerInputDto.setOffername("Offer Prueba Resource");
        this.offerInputDto.setEndDate(LocalDateTime.now());
        this.offerInputDto.setArticleLine(articleLinesResource);
        System.out.println(this.offerInputDto.toString());
    }

    @Test
    void testCreateOfferResource() {

    }
    /*@Test
    void testCreateOfferResource() {
        OfferOutputDto offerOutputDto = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<OfferOutputDto>())
                .clazz(OfferOutputDto.class)
                .path(OfferResource.OFFERS)
                .body(this.offerInputDto)
                .post()
                .build();
        System.out.println(offerOutputDto.toString());
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
