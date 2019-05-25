package es.upm.miw.business_controllers;

import es.upm.miw.business_services.PdfService;
import es.upm.miw.documents.*;
import es.upm.miw.dtos.ShoppingDto;
import es.upm.miw.dtos.TicketModificationStateOrAmountDto;
import es.upm.miw.dtos.input.TicketCreationInputDto;
import es.upm.miw.dtos.input.TicketQueryInputDto;
import es.upm.miw.dtos.output.TicketQueryOutputDto;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private GiftTicketRepository giftTicketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FamilyCompositeRepository familyCompositeRepository;

    @Autowired
    private CashierClosureRepository cashierClosureRepository;

    @Autowired
    private PdfService pdfService;

    private int nextId() {
        int nextId = 1;
        Ticket ticket = ticketRepository.findFirstByOrderByCreationDateDescIdDesc();
        if (ticket != null && ticket.getCreationDate().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN))) {
            nextId = ticket.simpleId() + 1;
        }
        return nextId;
    }

    private Ticket createTicket(TicketCreationInputDto ticketCreationDto) {
        User user = this.userRepository.findByMobile(ticketCreationDto.getUserMobile()).orElse(null);
        List<Shopping> shoppingList = new ArrayList<>();
        for (ShoppingDto shoppingDto : ticketCreationDto.getShoppingCart()) {
            Article article = this.articleRepository.findById(shoppingDto.getCode())
                    .orElseThrow(() -> new NotFoundException("Article (" + shoppingDto.getCode() + ")"));
            Shopping shopping = new Shopping(shoppingDto.getAmount(), shoppingDto.getDiscount(), article);
            if (shoppingDto.isCommitted()) {
                shopping.setShoppingState(ShoppingState.COMMITTED);
            } else {
                shopping.setShoppingState(ShoppingState.NOT_COMMITTED);
            }
            shoppingList.add(shopping);
            article.setStock(article.getStock() - shoppingDto.getAmount());
            this.articleRepository.save(article);
        }
        Ticket ticket = new Ticket(this.nextId(), ticketCreationDto.getVoucher(), ticketCreationDto.getCard(),
                ticketCreationDto.getCash(), shoppingList.toArray(new Shopping[0]), user);
        ticket.setNote(ticketCreationDto.getNote());

        GiftTicket giftTicket = new GiftTicket ("TR" + LocalDateTime.now().toString(),ticketCreationDto.getGiftNote());
        this.giftTicketRepository.save(giftTicket);

        ticket.setGiftTicket(giftTicket);

        this.ticketRepository.save(ticket);
        CashierClosure cashierClosure = this.cashierClosureRepository.findFirstByOrderByOpeningDateDesc();
        cashierClosure.voucher(ticketCreationDto.getVoucher());
        cashierClosure.cash(ticketCreationDto.getCash());
        cashierClosure.card(ticketCreationDto.getCard());
        this.cashierClosureRepository.save(cashierClosure);
        return ticket;
    }

    public List<TicketQueryOutputDto> advancedTicketQuery(TicketQueryInputDto ticketQueryDto) {
        String userMobile = ticketQueryDto.getUserMobile();
        LocalDateTime dateStart = ticketQueryDto.getDateStart();
        LocalDateTime dateEnd = ticketQueryDto.getDateEnd();
        BigDecimal totalMin = ticketQueryDto.getTotalMin();
        BigDecimal totalMax = ticketQueryDto.getTotalMax();
        List<TicketQueryOutputDto> ticketResults;
        List<TicketQueryOutputDto> ticketsFoundByMobile;
        List<TicketQueryOutputDto> ticketsFoundByDateRange;
        List<TicketQueryOutputDto> ticketsFoundByTotalRange;
        Boolean findByTotalRange = totalMin != null && totalMax != null;
        Boolean findByUserMobile = userMobile != null;
        Boolean findByDateRange = dateStart != null && dateEnd != null;
        Boolean findByPending = ticketQueryDto.getPending();
        boolean findByPendingOnly = findByPending && !findByUserMobile && !findByDateRange && !findByTotalRange;

        if (findByPendingOnly) {
            return this.findTicketsByPending();
        }

        ticketsFoundByMobile = this.findTicketByMobile(userMobile);
        ticketsFoundByDateRange = this.findTicketsByDateRange(dateStart, dateEnd);
        ticketsFoundByTotalRange = this.findTicketsByTotalRange(totalMin, totalMax);

        //LogManager.getLogger().debug("ticketsFoundByMobile: >>>>> " + ticketsFoundByMobile.size());
        //LogManager.getLogger().debug("ticketsFoundByDateRange: >>>>> " + ticketsFoundByDateRange.size());
        //LogManager.getLogger().debug("ticketsFoundByTotalRange: >>>>> " + ticketsFoundByTotalRange.size());

        ticketResults = processTicketResults(ticketsFoundByMobile, ticketsFoundByDateRange, ticketsFoundByTotalRange,
                findByTotalRange, findByUserMobile, findByDateRange);

        if(findByPending) {
            ticketResults = this.getPendingTickets(this.getTicketsFromOutputDto(ticketResults));
        }

        if(ticketResults.isEmpty()) {
            throw new NotFoundException("No matching tickets found");
        } else {
            return ticketResults;
        }
    }

    private List<Ticket> getTicketsFromOutputDto(List<TicketQueryOutputDto> ticketResults) {
        List<Ticket> tickets = new ArrayList<>();
        for(TicketQueryOutputDto item: ticketResults) {
            this.ticketRepository.findById(item.getId()).ifPresent(tickets::add);
        }
        return tickets;
    }

    private List<TicketQueryOutputDto> findTicketsByPending() {
        List<Ticket> allTickets = this.ticketRepository.findAll();
        List<TicketQueryOutputDto> results;
        results = this.getPendingTickets(allTickets);
        return results;
    }

    private List<TicketQueryOutputDto> getPendingTickets(List<Ticket> allTickets) {
        List<TicketQueryOutputDto> results = new ArrayList<>();
        for(Ticket item: allTickets) {
            for(Shopping article : item.getShoppingList()) {
                if(article.getShoppingState() == ShoppingState.NOT_COMMITTED) {
                    results.add(new TicketQueryOutputDto(item));
                }
            }
        }
        return results;
    }

    private List<TicketQueryOutputDto> processTicketResults(List<TicketQueryOutputDto> ticketsFoundByMobile,
                                                            List<TicketQueryOutputDto> ticketsFoundByDateRange,
                                                            List<TicketQueryOutputDto> ticketsFoundByTotalRange,
                                                            Boolean findByTotalRange, Boolean findByUserMobile,
                                                            Boolean findByDateRange) {
        List<TicketQueryOutputDto> ticketResults = new ArrayList<>();

        if(findByUserMobile && findByDateRange && findByTotalRange) {
            ticketResults = getCompositeSearchResults(ticketsFoundByMobile, ticketsFoundByDateRange);
            ticketResults = getCompositeSearchResults(ticketResults, ticketsFoundByTotalRange);
            return ticketResults;
        } else if(findByUserMobile && findByDateRange) {
            return getCompositeSearchResults(ticketsFoundByMobile, ticketsFoundByDateRange);
        } else if(findByUserMobile && findByTotalRange) {
            return getCompositeSearchResults(ticketsFoundByMobile, ticketsFoundByTotalRange);
        } else if(findByDateRange && findByTotalRange) {
            return getCompositeSearchResults(ticketsFoundByDateRange, ticketsFoundByTotalRange);
        } else {
            ticketResults = (findByTotalRange)? ticketsFoundByTotalRange : ticketResults;
            ticketResults = (findByUserMobile)? ticketsFoundByMobile : ticketResults;
            ticketResults = (findByDateRange)? ticketsFoundByDateRange : ticketResults;
        }
        return ticketResults;
    }

    private List<TicketQueryOutputDto> findTicketsByTotalRange(BigDecimal totalMin, BigDecimal totalMax) {
        List<TicketQueryOutputDto> results = new ArrayList<>();
        List<Ticket> allTickets = this.ticketRepository.findAll();
        if(totalMin!=null && totalMax!=null) {
            for(Ticket ticket: allTickets) {
                Boolean greaterThanMin = ticket.getTotal().compareTo(totalMin)>0;
                Boolean equalThanMin = ticket.getTotal().compareTo(totalMin)==0;
                Boolean equalThanMax = ticket.getTotal().compareTo(totalMax)==0;
                Boolean lesserThanMax = ticket.getTotal().compareTo(totalMax)<0;
                if((greaterThanMin || equalThanMin) && (equalThanMax||lesserThanMax)) {
                    results.add(new TicketQueryOutputDto(ticket));
                }
            }
            return results;
        } else return results;
    }

    public byte[] createTicketAndPdf(TicketCreationInputDto ticketCreationDto) {
        return pdfService.generateTicket(this.createTicket(ticketCreationDto));
    }

    public byte[] generateGiftTicketController(String id) {
        if (id == null || id.isEmpty()){
            return pdfService.generateGiftTicket(this.ticketRepository.findFirstByOrderByCreationDateDescIdDesc());
        }else{
            return pdfService.generateGiftTicket(readTicketById(id));
        }
    }

    public Ticket createTicketForTests(TicketCreationInputDto ticketCreationDto) {
        return this.createTicket(ticketCreationDto);
    }

    private List<TicketQueryOutputDto> findTicketByMobile(String userMobile) {
        if(userMobile == null) {
            return new ArrayList<>();
        }
        User user = this.userRepository.findByMobile(userMobile)
                .orElseThrow(() -> new NotFoundException("User mobile:" + userMobile));
        return this.getQueryOutputDtoList(this.ticketRepository.findByUser(user.getId()));
    }

    private List<TicketQueryOutputDto> getQueryOutputDtoList(List<Ticket> list) {
        List<TicketQueryOutputDto> results = new ArrayList<>();
        for(Ticket ticket: list) {
            results.add(new TicketQueryOutputDto(ticket));
        }
        return results;
    }

    private List<TicketQueryOutputDto> findTicketsByDateRange(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return this.getQueryOutputDtoList(this.ticketRepository.findByCreationDateBetween(dateFrom, dateTo));
    }

    private Boolean listContainsTicket(List<TicketQueryOutputDto> list1, String ticketId) {
        List<String> listIds = new ArrayList<>();
        for(TicketQueryOutputDto item: list1) {
            listIds.add(item.getId());
        }
        return listIds.contains(ticketId);
    }

    private List<TicketQueryOutputDto> getCompositeSearchResults(List<TicketQueryOutputDto> itemsList1,
                                                                 List<TicketQueryOutputDto> itemsList2) {
        List<TicketQueryOutputDto> results = new ArrayList<>();
        for(TicketQueryOutputDto list1item: itemsList2) {
            if(this.listContainsTicket(itemsList1, list1item.getId())) {
                results.add(list1item);
            }
        }
        return results;
    }

    public List<TicketQueryOutputDto> advancedTicketQueryByOrderId(TicketQueryInputDto ticketQueryDto) {
        List<TicketQueryOutputDto> ticketResults = new ArrayList<>();
        Boolean findByPending = ticketQueryDto.getPending();
        if(ticketQueryDto.getOrderId()!=null) {
            Order searchOrder = this.orderRepository.findById(ticketQueryDto.getOrderId())
                    .orElseThrow(() -> new NotFoundException("Order ID not found"));
            ticketResults = this.findTicketsWithArticlesFromOrder(searchOrder);
        }
        ticketResults = (findByPending) ?
                this.getPendingTickets(this.getTicketsFromOutputDto(ticketResults)) : ticketResults;
        return ticketResults;
    }

    private List<TicketQueryOutputDto> findTicketsWithArticlesFromOrder(Order searchOrder) {
        List<TicketQueryOutputDto> results = new ArrayList<>();
        List<Ticket> allTickets = this.ticketRepository.findAll();
        List<Article> articlesFromOrder = this.getArticlesFromOrder(searchOrder);
        for(Article orderItem: articlesFromOrder) {
            for(Ticket ticketItem: allTickets) {
                for(Article ticketArticle: this.getArticlesFromTicket(ticketItem)) {
                    if(orderItem.getCode().equalsIgnoreCase(ticketArticle.getCode())) {
                        results.add(new TicketQueryOutputDto(ticketItem));
                    }
                }
            }
        }
        return results;
    }

    private List<Article> getArticlesFromOrder(Order searchOrder) {
        List<Article> results = new ArrayList<>();
        for(OrderLine orderLine: searchOrder.getOrderLines()) {
            results.add(orderLine.getArticle());
        }
        return results;
    }

    private List<Article> getArticlesFromTicket(Ticket ticket) {
        List<Article> results = new ArrayList<>();
        for(Shopping shoppingList: ticket.getShoppingList()) {
            results.add(shoppingList.getArticle());
        }
        return results;
    }

    public TicketModificationStateOrAmountDto obtainTicketModifiedById(String id) {
        boolean isGiftTicket = false;

        Ticket ticket = this.ticketRepository.findById(id).orElse(new Ticket());

        if(ticket.getId()==null || ticket.getId().isEmpty() || "null".equals(ticket.getId())){
            isGiftTicket = true;
            ticket = this.ticketRepository.findByGiftTicket(id).orElseThrow(() -> new NotFoundException("Ticket id (" + id + ")"));
        }

        return new TicketModificationStateOrAmountDto(ticket, isGiftTicket&&ticket.getGiftTicket().isGiftTicketExpired());
    }

    public Ticket readTicketById(String id) {
        return (this.ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket id (" + id + ")")));
    }

    public Ticket updateModifiedTicket(String id, TicketModificationStateOrAmountDto modifiedTicket) {
        Ticket ticketDb = this.readTicketById(id);
        List<Shopping> shoppings = new ArrayList<>();
        modifiedTicket.getShoppingList().forEach(p ->
            shoppings.add(p.transformModifiedShoppingToShopping()));
        int i  = 0;
        for (Shopping shopping : ticketDb.getShoppingList()){
            shopping.setAmount(shoppings.get(i).getAmount());
            shopping.setShoppingState(shoppings.get(i).getShoppingState());
            i++;
        }
        this.ticketRepository.save(ticketDb);
        return ticketDb;
    }

    public byte[] updateModifiedTicketAndPdf(String id, TicketModificationStateOrAmountDto modifiedTicket) {
        return pdfService.generateTicket(this.updateModifiedTicket(id, modifiedTicket));
    }

    private LocalDateTime convertStringToDate(String datesold){
        return LocalDateTime.parse(datesold);
    }

    public List<Article> getDateSold(String datesold) {
        List<Ticket> tickets =
                this.ticketRepository.findByCreationDateBetween(this.convertStringToDate(datesold), LocalDateTime.now());
        List<Article>  results = new ArrayList<>();
        for (Ticket ticket : tickets) {
            Shopping[] shoppingList = ticket.getShoppingList();
            for (int i = 0; i < shoppingList.length; i++) {
                results.add(shoppingList[i].getArticle());
            }
        }
        return results;
    }
}
