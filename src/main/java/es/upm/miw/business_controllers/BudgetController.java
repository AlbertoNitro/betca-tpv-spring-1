package es.upm.miw.business_controllers;

import es.upm.miw.dtos.BudgetDto;
import es.upm.miw.repositories.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BudgetController {

    @Autowired
    private BudgetRepository budgetRepository;

    public List<BudgetDto> readAll() {
        return this.budgetRepository.findAllBudgets();
    }

    public BudgetDto readById(String id) {
        return this.budgetRepository.findBudgetById(id);
    }

}
