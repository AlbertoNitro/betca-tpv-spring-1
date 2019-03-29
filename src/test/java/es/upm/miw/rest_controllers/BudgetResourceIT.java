package es.upm.miw.rest_controllers;

import es.upm.miw.documents.Article;
import es.upm.miw.documents.Shopping;
import es.upm.miw.dtos.BudgetDto;
import es.upm.miw.dtos.ShoppingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
class BudgetResourceIT {

    @Autowired
    private RestService restService;

    private BudgetDto existentBudget;


    @BeforeEach
    void before() {
        List<BudgetDto> budgets = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<BudgetDto[]>()).clazz(BudgetDto[].class)
                .path(BudgetResource.BUDGETS)
                .get().build());
        this.existentBudget = budgets.get(0);
    }

    @Test
    void testCreate() {
        ShoppingDto[] shoppings = new ShoppingDto[1];
        Shopping shopping = new Shopping(1, new BigDecimal(1), Article.builder("1").retailPrice("20").build());
        ShoppingDto shoppingDto = new ShoppingDto(shopping);
        shoppings[0] = shoppingDto;

        BudgetDto budgetDto = this.restService.loginAdmin().restBuilder(new RestBuilder<BudgetDto>())
                .clazz(BudgetDto.class).path(BudgetResource.BUDGETS).body(shoppings).post().build();
        assertNotNull(budgetDto);
        assertNotNull(budgetDto.getId());
    }

    @Test
    void testReadAll() {
        List<BudgetDto> budgetDtoList = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<BudgetDto[]>()).clazz(BudgetDto[].class)
                .path(BudgetResource.BUDGETS)
                .get().build());
        assertTrue(budgetDtoList.size() > 0);
    }

    @Test
    void testReadById() {
        BudgetDto budgetDto = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<BudgetDto>()).clazz(BudgetDto.class)
                .path(BudgetResource.BUDGETS)
                .path(BudgetResource.ID)
                .expand(this.existentBudget.getId())
                .get().build();
        assertNotNull(budgetDto);
        assertNotNull(budgetDto.getId());
    }

}
