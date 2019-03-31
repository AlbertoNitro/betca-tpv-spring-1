package es.upm.miw.business_controllers;

import es.upm.miw.business_services.PdfService;
import es.upm.miw.documents.Article;
import es.upm.miw.documents.Budget;
import es.upm.miw.documents.Shopping;
import es.upm.miw.dtos.BudgetDto;
import es.upm.miw.dtos.ShoppingDto;
import es.upm.miw.repositories.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class BudgetController {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private PdfService pdfService;

    private Budget create(ShoppingDto[] shoppingsDto) {
        Shopping[] shoppings = new Shopping[shoppingsDto.length];
        for(int i=0; i<shoppingsDto.length; i++) {
            shoppings[i] = new Shopping(shoppingsDto[i].getAmount(), shoppingsDto[i].getDiscount(),
                    Article.builder(shoppingsDto[i].getCode()).retailPrice(shoppingsDto[i].getRetailPrice())
                            .description(shoppingsDto[i].getDescription()).build());
        }

        Budget budget = new Budget(shoppings);
        this.budgetRepository.save(budget);

        return budget;
    }

    public byte[] createPdf(ShoppingDto[] shoppingsDto) {
        return this.pdfService.generateBudget(this.create(shoppingsDto));
    }

    public byte[] createPdfById(String id) {
        Optional<Budget> budget = this.budgetRepository.findById(id);
        if (budget.isPresent()) {
            return this.pdfService.generateBudget(budget.get());
        } else {
            return null;
        }
    }

    public void delete(String id) {
        Optional<Budget> budget = this.budgetRepository.findById(id);
        if (budget.isPresent()) {
            this.budgetRepository.delete(budget.get());
        }
    }


    public List<BudgetDto> readAll() {
        return this.budgetRepository.findAllBudgets();
    }

    public BudgetDto readById(String id) {
        return this.budgetRepository.findBudgetById(id);
    }

}
