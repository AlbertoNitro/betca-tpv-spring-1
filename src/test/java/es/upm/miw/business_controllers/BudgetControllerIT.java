package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Budget;
import es.upm.miw.documents.Shopping;
import es.upm.miw.dtos.BudgetDto;
import es.upm.miw.dtos.ShoppingDto;
import es.upm.miw.repositories.BudgetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
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
    private ShoppingDto[] shoppingsDto;
    private Shopping[] shoppings;

    @BeforeEach
    void seedDb() {
        this.shoppings = new Shopping[2];
        Shopping shopping = new Shopping(1, new BigDecimal(1), Article.builder("1").retailPrice("20")
                .description("Varios").build());
        Shopping shopping2 = new Shopping(2, new BigDecimal(1), Article.builder("1").retailPrice("5")
                .description("Varios2").build());
        this.shoppings[0] = shopping;
        this.shoppings[1] = shopping2;

        this.shoppingsDto = new ShoppingDto[2];
        ShoppingDto shoppingDto = new ShoppingDto(shopping);
        ShoppingDto shoppingDto2 = new ShoppingDto(shopping2);
        this.shoppingsDto[0] = shoppingDto;
        this.shoppingsDto[1] = shoppingDto2;

        this.budget = new Budget(this.shoppings);

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

    @Test
    void testCreatePdf() {
        byte[] budgetPdf = budgetController.createPdf(this.shoppingsDto);
        assertNotNull(budgetPdf);
    }

    @Test
    void testCreatePdfById() {
        byte[] budgetPdf = budgetController.createPdfById(this.budget.getId());
        assertNotNull(budgetPdf);
    }

    @Test
    void testDelete() {
        List<BudgetDto> budgets = budgetController.readAll();
        Integer size = budgets.size();
        budgetController.delete(this.budget.getId());
        budgets = budgetController.readAll();
        assertTrue(budgets.size() == size - 1);
    }

    @AfterEach
    void delete() {
        this.budgetRepository.delete(budget);
    }

}
