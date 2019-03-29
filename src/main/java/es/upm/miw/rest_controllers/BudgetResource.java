package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.BudgetDto;
import es.upm.miw.business_controllers.BudgetController;
import es.upm.miw.dtos.ShoppingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(BudgetResource.BUDGETS)
public class BudgetResource {

    public static final String BUDGETS = "/budgets";

    public static final String ID = "/{id}";

    @Autowired
    private BudgetController budgetController;

    @PostMapping()
    public BudgetDto create(@Valid @RequestBody ShoppingDto[] shopping) {
        return this.budgetController.create(shopping);
    }

    @DeleteMapping(value = ID)
    public void delete(@PathVariable String id) {
        this.budgetController.delete(id);
    }
    
    @GetMapping
    public List<BudgetDto> readAll() {
        return this.budgetController.readAll();
    }

    @GetMapping(value = ID)
    public BudgetDto readById(@PathVariable String id) {
        return this.budgetController.readById(id);
    }

}
