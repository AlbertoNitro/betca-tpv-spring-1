package es.upm.miw.business_controllers;

import es.upm.miw.documents.Article;
import es.upm.miw.documents.Budget;
import es.upm.miw.documents.Shopping;
import es.upm.miw.dtos.BudgetDto;
import es.upm.miw.dtos.ShoppingDto;
import es.upm.miw.repositories.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BudgetController {

    @Autowired
    private BudgetRepository budgetRepository;

    public BudgetDto create(ShoppingDto[] shoppingsDto) {
        Shopping[] shoppings = new Shopping[shoppingsDto.length];
        for(int i=0; i<shoppingsDto.length; i++) {
            shoppings[i] = new Shopping(shoppingsDto[i].getAmount(), shoppingsDto[i].getDiscount(),
                    Article.builder(shoppingsDto[i].getCode()).retailPrice(shoppingsDto[i].getRetailPrice()).build());
        }

        Budget budget = new Budget(shoppings);
        this.budgetRepository.save(budget);

        return new BudgetDto(budget);
    }

    public List<BudgetDto> readAll() {
        return this.budgetRepository.findAllBudgets();
    }

    public BudgetDto readById(String id) {
        return this.budgetRepository.findBudgetById(id);
    }

}
