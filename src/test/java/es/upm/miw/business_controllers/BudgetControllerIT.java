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

    private ShoppingDto[] createShoppingListDto() {
        ShoppingDto[] shoppings = new ShoppingDto[2];
        Shopping shopping = new Shopping(1, new BigDecimal(1), Article.builder("1").retailPrice("20")
                .description("Varios").build());
        Shopping shopping2 = new Shopping(2, new BigDecimal(1), Article.builder("2").retailPrice("5")
                .description("Varios2").build());

        ShoppingDto shoppingDto = new ShoppingDto(shopping);
        ShoppingDto shoppingDto2 = new ShoppingDto(shopping2);
        shoppings[0] = shoppingDto;
        shoppings[1] = shoppingDto2;

        return shoppings;
    }

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

    @Test
    void testCreate() {
        ShoppingDto[] shoppings = this.createShoppingListDto();
        byte[] budgetPdf = budgetController.createPdf(shoppings);
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
