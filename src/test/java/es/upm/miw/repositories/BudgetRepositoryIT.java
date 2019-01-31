package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class BudgetRepositoryIT {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void testReadAll() {
        Budget budget = new Budget(this.ticketRepository.findById("201901121").get().getShoppingList());
        this.budgetRepository.save(budget);
        assertTrue(this.budgetRepository.findAll().size() > 0);
        this.budgetRepository.delete(budget);
    }

}
