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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Shopping[] getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(Shopping[] shoppingList) {
        this.shoppingList = shoppingList;
    }
}