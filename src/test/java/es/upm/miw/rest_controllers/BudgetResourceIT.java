package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.BudgetDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
