package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.RgpdDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ApiTestConfig
public class RgpdResourceIT {

    @Autowired
    private RestService restService;

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
}
