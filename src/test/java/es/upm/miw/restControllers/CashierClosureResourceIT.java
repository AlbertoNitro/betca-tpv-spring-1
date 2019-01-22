package es.upm.miw.restControllers;

import es.upm.miw.dtos.CashierClosingOutputDto;
import es.upm.miw.dtos.CashierClosureInputDto;
import es.upm.miw.dtos.CashierLastOutputDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
public class CashierClosureResourceIT {

    @Autowired
    private RestService restService;

    @Test
    public void testGetCashierClosureLast() {
        CashierLastOutputDto cashierClosureLastDto = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<CashierLastOutputDto>()).clazz(CashierLastOutputDto.class)
                .path(CashierClosureResource.CASHIER_CLOSURES).path(CashierClosureResource.LAST)
                .get().build();
        assertTrue(cashierClosureLastDto.isClosed());
    }

    @Test
    public void testGetCashierClosureLastTotals() {
        this.restService.loginAdmin().restBuilder().path(CashierClosureResource.CASHIER_CLOSURES)
                .post().build();
        CashierClosingOutputDto cashierClosingOutputDto = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<CashierClosingOutputDto>()).clazz(CashierClosingOutputDto.class)
                .path(CashierClosureResource.CASHIER_CLOSURES).path(CashierClosureResource.LAST)
                .path(CashierClosureResource.TOTALS)
                .get().build();
        assertNotNull(cashierClosingOutputDto);
        CashierClosureInputDto cashierClosureInputDto = new CashierClosureInputDto(BigDecimal.ZERO, BigDecimal.ZERO, "");
        this.restService.loginAdmin().restBuilder()
                .path(CashierClosureResource.CASHIER_CLOSURES).path(CashierClosureResource.LAST)
                .body(cashierClosureInputDto)
                .patch().build();
    }

    @Test
    public void testGetCashierClosureLastTotalsCashierClosed() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder()
                        .path(CashierClosureResource.CASHIER_CLOSURES).path(CashierClosureResource.LAST)
                        .path(CashierClosureResource.TOTALS)
                        .get().build());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }


}
