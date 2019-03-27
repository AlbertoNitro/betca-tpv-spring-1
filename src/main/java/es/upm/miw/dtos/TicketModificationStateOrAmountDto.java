package es.upm.miw.dtos;

import es.upm.miw.documents.Shopping;
import es.upm.miw.documents.Ticket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TicketModificationStateOrAmountDto {

    private String id;

    private List<ShoppingModificationStateOrAmountDto> shoppingList;

    public TicketModificationStateOrAmountDto(Ticket ticket) {
        this.id = ticket.getId();
        this.shoppingList = this.initializeShoppingTicket(ticket.getShoppingList());
    }

    private List<ShoppingModificationStateOrAmountDto> initializeShoppingTicket(Shopping[] shoppings) {

        List<ShoppingModificationStateOrAmountDto> shoppingModificationDto = new ArrayList<>();
        Arrays.stream(shoppings).forEach(p -> {
            shoppingModificationDto.add(new ShoppingModificationStateOrAmountDto(p));
        });
        return shoppingModificationDto;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<ShoppingModificationStateOrAmountDto> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<ShoppingModificationStateOrAmountDto> shoppingList) {
        this.shoppingList = shoppingList;
    }


    @Override
    public String toString() {
        return "TicketModificationStateOrAmountDto{" +
                "id=" + id +
                "shoppingList=" + shoppingList.toString() +
                '}';
    }
}
