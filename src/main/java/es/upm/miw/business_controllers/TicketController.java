package es.upm.miw.business_controllers;

import es.upm.miw.business_services.PdfService;
import es.upm.miw.documents.*;
import es.upm.miw.dtos.ShoppingDto;
import es.upm.miw.dtos.TicketCreationInputDto;
import es.upm.miw.dtos.TicketQueryInputDto;
import es.upm.miw.dtos.TicketQueryOutputDto;
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

        ticketsFoundByMobile = this.findTicketByMobile(userMobile);
        ticketsFoundByDateRange = this.findTicketsByDateRange(dateStart, dateEnd);
        ticketsFoundByTotalRange = this.findTicketsByTotalRange(totalMin, totalMax);

        LogManager.getLogger().debug("ticketsFoundByMobile: >>>>> " + ticketsFoundByMobile.size());
        LogManager.getLogger().debug("ticketsFoundByDateRange: >>>>> " + ticketsFoundByDateRange.size());
        LogManager.getLogger().debug("ticketsFoundByTotalRange: >>>>> " + ticketsFoundByTotalRange.size());

        ticketResults = processTicketResults(userMobile, dateStart, dateEnd, totalMin, totalMax,
                ticketsFoundByMobile, ticketsFoundByDateRange, ticketsFoundByTotalRange);
/*
        if(userMobile!=null && (dateStart!=null && dateEnd!=null)) {
            LogManager.getLogger().debug("++++++++ Composite Search: By Mobile AND Date Range ++++++++");
            ticketResults = getCompositeSearchResults(ticketsFoundByMobile, ticketsFoundByDateRange);
            ticketResults = (totalMin!=null&&totalMax!=null)
                    ? getCompositeSearchResults(ticketResults, ticketsFoundByTotalRange) : ticketResults;
        } else if(ticketResults.isEmpty()) {
            ticketResults = (totalMin==null&&totalMax==null)? ticketResults : ticketsFoundByTotalRange;
            ticketResults = (userMobile==null)? ticketResults : ticketsFoundByMobile;
            ticketResults = (dateStart==null&&dateEnd==null)? ticketResults : ticketsFoundByDateRange;
            //Check for composite search AND by Date Range as well
            ticketResults = ((totalMin!=null&&totalMax!=null)
                    && (userMobile!=null || (dateStart!=null && dateEnd!=null)))
                    ? getCompositeSearchResults(ticketResults, ticketsFoundByTotalRange) : ticketResults;
        }
*/
        if(ticketResults.isEmpty()) {
            throw new NotFoundException("No matching tickets found");
        } else {
            return ticketResults;
        }
    }

    private List<TicketQueryOutputDto> processTicketResults(String userMobile, LocalDateTime dateStart,
                                                            LocalDateTime dateEnd, BigDecimal totalMin, BigDecimal totalMax,
                                                            List<TicketQueryOutputDto> ticketsFoundByMobile,
                                                            List<TicketQueryOutputDto> ticketsFoundByDateRange,
                                                            List<TicketQueryOutputDto> ticketsFoundByTotalRange) {
        List<TicketQueryOutputDto> ticketResults = new ArrayList<>();
        Boolean findByMobileAndDateRange = userMobile!=null && dateStart!=null && dateEnd!=null;
        Boolean findByTotalRange = totalMin != null && totalMax != null;
        Boolean findByUserMobile = userMobile != null;
        Boolean findByDateRange = dateStart != null && dateEnd != null;

        if(findByMobileAndDateRange && findByTotalRange) {
            ticketResults = getCompositeSearchResults(ticketsFoundByMobile, ticketsFoundByDateRange);
            ticketResults = getCompositeSearchResults(ticketResults, ticketsFoundByTotalRange);
            return ticketResults;
        } else if(findByMobileAndDateRange) {
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

    public Ticket createTicketForTests(TicketCreationInputDto ticketCreationDto) {
        return this.createTicket(ticketCreationDto);
    }

    private List<TicketQueryOutputDto> findTicketByMobile(String userMobile) {
        if(userMobile == null) {
            return new ArrayList<>();
        }
        User user = this.userRepository.findByMobile(userMobile)
                .orElseThrow(() -> new NotFoundException("User mobile:" + userMobile));
        return this.ticketRepository.findByUser(user.getId());
    }

    private List<TicketQueryOutputDto> findTicketsByDateRange(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return this.ticketRepository.findByDateRange(dateFrom, dateTo);
    }

    private Boolean listContainsTicket(List<TicketQueryOutputDto> list1, String ticketId) {
        List<String> listIds = new ArrayList<>();
        for(TicketQueryOutputDto item: list1) {
            listIds.add(item.getId());
        }
        return listIds.contains(ticketId);
    }

    private List<TicketQueryOutputDto> getCompositeSearchResults(List<TicketQueryOutputDto> ticketsFoundByMobile,
                                                                 List<TicketQueryOutputDto> ticketsFoundByDateRange) {
        List<TicketQueryOutputDto> results = new ArrayList<>();
        for(TicketQueryOutputDto dateRangeItem: ticketsFoundByDateRange) {
            if(this.listContainsTicket(ticketsFoundByMobile, dateRangeItem.getId())) {
                results.add(dateRangeItem);
            }
        }
        return results;
    }

}
