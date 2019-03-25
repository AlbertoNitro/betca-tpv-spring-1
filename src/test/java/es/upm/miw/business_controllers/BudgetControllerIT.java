package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.dtos.BudgetDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class BudgetControllerIT {

    @Autowired
    private BudgetController budgetController;


    @Test
    void testReadAll() {
        List<BudgetDto> budgets = budgetController.readAll();
        System.out.println(budgets);
        assertTrue(budgets.size() > 0);
    }

}
