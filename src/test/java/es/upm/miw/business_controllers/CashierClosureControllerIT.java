package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.data_services.DatabaseSeederService;
import es.upm.miw.dtos.CashMovementInputDto;
import es.upm.miw.dtos.CashierLastOutputDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class CashierClosureControllerIT {

    @Autowired
    private CashierClosureController cashierClosureController;

    @Autowired
    private DatabaseSeederService databaseSeederService;

    @Test
    void testReadCashierClosureLast() {
        CashierLastOutputDto cashierLastOutputDto = cashierClosureController.readCashierClosureLast();
        assertNotNull(cashierLastOutputDto);
        assertTrue(cashierLastOutputDto.isClosed());
    }

    @Test
    void testDeposit() {
        CashierLastOutputDto cashierLastOutputDto = cashierClosureController.readCashierClosureLast();
        cashierClosureController.createCashierClosure();
        cashierLastOutputDto = cashierClosureController.readCashierClosureLast();
        assertNotNull(cashierLastOutputDto);
        assertFalse(cashierLastOutputDto.isClosed());
        CashMovementInputDto cashMovementInputDto = new CashMovementInputDto(new BigDecimal(100), "deposit 100e");
        cashierClosureController.deposit(cashMovementInputDto);
    }

    @Test
    void testWithdrawal() {
        CashierLastOutputDto cashierLastOutputDto = cashierClosureController.readCashierClosureLast();
        assertNotNull(cashierLastOutputDto);
        assertFalse(cashierLastOutputDto.isClosed());
        CashMovementInputDto cashMovementInputDto = new CashMovementInputDto(new BigDecimal(100), "withdrawal 200e");
        cashierClosureController.withdrawal(cashMovementInputDto);
    }

}
