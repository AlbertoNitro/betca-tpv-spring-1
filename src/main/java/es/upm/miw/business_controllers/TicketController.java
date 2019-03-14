package es.upm.miw.business_controllers;

import es.upm.miw.business_services.PdfService;
import es.upm.miw.documents.*;
import es.upm.miw.dtos.ShoppingDto;
import es.upm.miw.dtos.TicketCreationInputDto;
import es.upm.miw.dtos.TicketQueryInputDto;
import es.upm.miw.dtos.TicketQueryResultDto;
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

    public List<TicketQueryResultDto> advancedTicketQuery(TicketQueryInputDto ticketQueryDto) {
        String userMobile = ticketQueryDto.getUserMobile();
        LocalDateTime dateStart = ticketQueryDto.getDateStart();
        LocalDateTime dateEnd = ticketQueryDto.getDateEnd();
        BigDecimal totalMin = ticketQueryDto.getTotalMin();
        BigDecimal totalMax = ticketQueryDto.getTotalMax();
        Boolean pending = ticketQueryDto.getPending();
        List<TicketQueryResultDto> ticketResults = new ArrayList<>();
        List<TicketQueryResultDto> ticketsFoundByMobile = new ArrayList<>();
        List<TicketQueryResultDto> ticketsFoundByDateRange = new ArrayList<>();

        ticketsFoundByMobile = this.findTicketByMobile(userMobile);
        ticketsFoundByDateRange = this.findTicketsByDateRange(dateStart, dateEnd);

        LogManager.getLogger().warn("ticketsFoundByMobile: >>>>> " + ticketsFoundByMobile.size());
        LogManager.getLogger().warn("ticketsFoundByDateRange: >>>>> " + ticketsFoundByDateRange.size());

        if (userMobile!=null && (dateStart!=null && dateEnd!=null)) {
            LogManager.getLogger().warn("++++++++ Composite Search: By Mobile AND Date Range ++++++++");
            for(TicketQueryResultDto dateRangeItem: ticketsFoundByDateRange){
                for(TicketQueryResultDto mobileItem: ticketsFoundByMobile) {
                    if(dateRangeItem.getId().equals(mobileItem.getId())) {
                        LogManager.getLogger().warn("ticketsFoundByMobile List CONTAINS " + dateRangeItem.getId());
                        ticketResults.add(dateRangeItem);
                    }
                }
            }
        } else if(ticketResults.isEmpty()) {
            ticketResults = (userMobile==null)? ticketResults : ticketsFoundByMobile;
            ticketResults = (dateStart==null&&dateEnd==null)? ticketResults : ticketsFoundByDateRange;
        }

        if(ticketResults.isEmpty()) {
            throw new NotFoundException("No matching tickets found");
        } else {
            return ticketResults;
        }
    }

    public byte[] createTicketAndPdf(TicketCreationInputDto ticketCreationDto) {
        return pdfService.generateTicket(this.createTicket(ticketCreationDto));
    }

    public Ticket createTicketForTests(TicketCreationInputDto ticketCreationDto) {
        return this.createTicket(ticketCreationDto);
    }

    private List<TicketQueryResultDto> findTicketByMobile(String userMobile) {
        if(userMobile == null) {
            return new ArrayList<>();
        }
        User user = this.userRepository.findByMobile(userMobile)
                .orElseThrow(() -> new NotFoundException("User mobile:" + userMobile));
        return this.ticketRepository.findByUser(user.getId());
    }

    private List<TicketQueryResultDto> findTicketsByDateRange(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return this.ticketRepository.findByDateRange(dateFrom, dateTo);
    }

}
