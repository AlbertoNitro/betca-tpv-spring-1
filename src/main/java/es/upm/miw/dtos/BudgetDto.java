package es.upm.miw.dtos;

import es.upm.miw.documents.Budget;
import es.upm.miw.documents.Shopping;

import java.time.LocalDateTime;

public class BudgetDto {

    private String id;

    private LocalDateTime creationDate;

    private Shopping[] shoppingList;

    public BudgetDto() {
        // Empty for framework
    }

    public BudgetDto(Budget budget) {
        this.id = budget.getId();
        this.creationDate = budget.getCreationDate();
        this.shoppingList = budget.getShoppingList();
    }
}