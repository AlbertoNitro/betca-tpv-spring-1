package es.upm.miw.repositories;

import es.upm.miw.documents.Budget;
import es.upm.miw.dtos.BudgetDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BudgetRepository extends MongoRepository<Budget, String> {

    @Query(value = "{}", fields = "{ '_id' : 1, 'creationDate' : 1, 'shoppingList' : 1}")
    List<BudgetDto> findAllBudgets();

    @Query(value = "{ '_id' : ?0 }", fields = "{ '_id' : 1, 'creationDate' : 1, 'shoppingList' : 1}")
    BudgetDto findBudgetById(String id);

}
