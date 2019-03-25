package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.BudgetDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
class BudgetResourceIT {

    @Autowired
    private RestService restService;

    @Test
    void testReadAll() {
        List<BudgetDto> budgetDtoList = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<BudgetDto[]>()).clazz(BudgetDto[].class)
                .path(BudgetResource.BUDGETS)
                .get().build());
        assertTrue(budgetDtoList.size() > 0);
    }

}
