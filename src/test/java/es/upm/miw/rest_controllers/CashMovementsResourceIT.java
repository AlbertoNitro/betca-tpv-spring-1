package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.CashMovementInputDto;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
class CashMovementsResourceIT {

    @Autowired
    private RestService restService;

    @Test
    void testPostDeposit() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder()
                        .path(CashMovementsResource.CASH_MOVEMENTS)
                        .path(CashMovementsResource.DEPOSIT)
                        .post().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        CashMovementInputDto cashMovementInputDto = new CashMovementInputDto(BigDecimal.TEN, "deposit 10e");
        HttpResponse response = (HttpResponse) this.restService.loginAdmin().restBuilder()
                .path(CashMovementsResource.CASH_MOVEMENTS)
                .path(CashMovementsResource.DEPOSIT)
                .body(cashMovementInputDto)
                .post().build();
        assertNull(response);
    }

    @Test
    void testPostWithdrawal() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder()
                        .path(CashMovementsResource.CASH_MOVEMENTS)
                        .path(CashMovementsResource.WITHDRAWAL)
                        .post().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        CashMovementInputDto cashMovementInputDto = new CashMovementInputDto(BigDecimal.TEN, "withdrawl 10e");
        HttpResponse response = (HttpResponse) this.restService.loginAdmin().restBuilder()
                .path(CashMovementsResource.CASH_MOVEMENTS)
                .path(CashMovementsResource.WITHDRAWAL)
                .body(cashMovementInputDto)
                .post().build();
        assertNull(response);
    }
}
