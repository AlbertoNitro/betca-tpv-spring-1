package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Budget;
import es.upm.miw.dtos.BudgetDto;
import es.upm.miw.repositories.BudgetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class BudgetControllerIT {

    @Autowired
    private BudgetController budgetController;

    @Autowired
    private BudgetRepository budgetRepository;

    private Budget budget;
    private String idBudget2;


    @BeforeEach
    void seedDb() {
        this.budget = new Budget();
        this.budgetRepository.save(budget);
    }

    @Test
    void testReadAll() {
        List<BudgetDto> budgets = budgetController.readAll();
        System.out.println(budgets);
        assertTrue(budgets.size() > 0);
    }

    @Test
    void testReadById() {
        BudgetDto budget = budgetController.readById(this.budget.getId());
        System.out.println(budget);
        assertNotNull(budget);
        assertNotNull(budget.getId());
    }


    @AfterEach
    void delete() {
        this.budgetRepository.delete(budget);
    }

}
