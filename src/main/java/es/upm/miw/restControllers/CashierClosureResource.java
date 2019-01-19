package es.upm.miw.restControllers;

import es.upm.miw.businessControllers.CashierClosureController;
import es.upm.miw.dtos.CashierLastOutputDto;
import es.upm.miw.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(CashierClosureResource.CASHIER_CLOSURES)
public class CashierClosureResource {

    public static final String CASHIER_CLOSURES = "/cashier-closures";

    public static final String LAST = "/last";

    public static final String MOVEMENTS = "/movements";

    public static final String TOTALS = "/totals";

    public static final String SEARCH = "/search";

    public static final String DATE = "/date";

    @Autowired
    private CashierClosureController cashierClosureController;

    @PostMapping
    public void openCashierClosure() throws BadRequestException {
        cashierClosureController.createCashierClosure();
    }

    @GetMapping(value = LAST)
    public CashierLastOutputDto getCashierClosureLast() {
        return cashierClosureController.readCashierClosureLast();
    }

}
