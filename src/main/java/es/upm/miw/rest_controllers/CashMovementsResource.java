package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.CashierClosureController;
import es.upm.miw.dtos.input.CashMovementInputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(CashMovementsResource.CASH_MOVEMENTS)
public class CashMovementsResource {

    public static final String CASH_MOVEMENTS = "/cash-movements";

    public static final String DEPOSIT = "/deposit";

    public static final String WITHDRAWAL = "/withdrawal";

    @Autowired
    private CashierClosureController cashierClosureController;

    @PostMapping(value = DEPOSIT)
    public void depositCashMovement(@RequestBody CashMovementInputDto cashMovementInputDto){
        cashierClosureController.deposit(cashMovementInputDto);
    }

    @PostMapping(value = WITHDRAWAL)
    public void withdrawalCashMovement(@RequestBody CashMovementInputDto cashMovementInputDto){
        cashierClosureController.withdrawal(cashMovementInputDto);
    }
}
