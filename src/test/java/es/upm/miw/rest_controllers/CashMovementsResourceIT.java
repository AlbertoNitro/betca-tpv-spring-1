package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.input.CashMovementInputDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ApiTestConfig
class CashMovementsResourceIT {

    @Autowired
    private RestService restService;

    @Test
    void testPostDepositCashierNotOpened() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder()
                        .path(CashMovementsResource.CASH_MOVEMENTS)
                        .path(CashMovementsResource.DEPOSIT)
                        .post().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        CashMovementInputDto cashMovementInputDto = new CashMovementInputDto(BigDecimal.TEN, "deposit 10e");
        HttpClientErrorException exception2 = assertThrows(HttpClientErrorException.class, () ->this.restService.loginAdmin().restBuilder()
                .path(CashMovementsResource.CASH_MOVEMENTS)
                .path(CashMovementsResource.DEPOSIT)
                .body(cashMovementInputDto)
                .post().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception2.getStatusCode());
    }

    @Test
    void testPostWithdrawalCashierNotOpened() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder()
                        .path(CashMovementsResource.CASH_MOVEMENTS)
                        .path(CashMovementsResource.WITHDRAWAL)
                        .post().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        CashMovementInputDto cashMovementInputDto = new CashMovementInputDto(BigDecimal.TEN, "withdrawl 10e");
        HttpClientErrorException exception2 = assertThrows(HttpClientErrorException.class, () ->this.restService.loginAdmin().restBuilder()
                .path(CashMovementsResource.CASH_MOVEMENTS)
                .path(CashMovementsResource.WITHDRAWAL)
                .body(cashMovementInputDto)
                .post().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception2.getStatusCode());
    }
}
