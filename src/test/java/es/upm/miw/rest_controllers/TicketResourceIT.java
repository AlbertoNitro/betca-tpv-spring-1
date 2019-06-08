package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.TicketController;
import es.upm.miw.documents.*;
import es.upm.miw.dtos.*;
import es.upm.miw.dtos.input.TicketCreationInputDto;
import es.upm.miw.dtos.input.TicketQueryInputDto;
import es.upm.miw.dtos.output.TicketQueryOutputDto;
import es.upm.miw.repositories.ArticleRepository;
import es.upm.miw.repositories.OrderRepository;
import es.upm.miw.repositories.TicketRepository;
import es.upm.miw.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
class TicketResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketController ticketController;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private OrderRepository orderRepository;

    private List<Ticket> initialTicketDB;

    @BeforeEach
    void backupTicketDB() {
        initialTicketDB = this.ticketRepository.findAll();
    }

    @AfterEach
    void resetTicketDB() {
        this.ticketRepository.deleteAll();
        this.ticketRepository.saveAll(this.initialTicketDB);
    }

    @Test
    void testCreateTicket() {
        ShoppingDto shoppingDto =
                new ShoppingDto("1", "", new BigDecimal("100.00"), 1, BigDecimal.ZERO,
                        new BigDecimal("100.00"), true);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(null, new BigDecimal("100.00")
                , BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto), "Nota del ticket...", "Nota Regalo");
        byte[] pdf = this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).body(ticketCreationInputDto)
                .post().build();
        assertNotNull(pdf);
    }

    @Test
    void testCreateGiftTicketWithNullId() {
        byte[] pdf = this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).path(TicketResource.GIFT).param("id", null)
                .get().build();
        assertNotNull(pdf);
    }

    @Test
    void testCreateGiftTicketNull() {
        byte[] pdf = this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).path(TicketResource.GIFT).get().build();
        assertNotNull(pdf);
    }

    @Test
    void testCreateGiftTicketNotFoundId() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder().path(TicketResource.TICKETS)
                        .path(TicketResource.GIFT).param("id", "invalid id")
                        .get().build());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testCreateGiftTicketWithId() {
        byte[] pdf = this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).path(TicketResource.GIFT).param("id", "201901121")
                .get().build();
        assertNotNull(pdf);
    }

    @Test
    void testCreateReserve() {
        ShoppingDto shoppingDto =
                new ShoppingDto("1", "", new BigDecimal("100.00"), 1, BigDecimal.ZERO,
                        new BigDecimal("100.00"), false);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto("666666004",
                BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, Arrays.asList(shoppingDto),
                "Nota del ticket...", "Nota Regalo");
        byte[] pdf = this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).body(ticketCreationInputDto)
                .post().build();
        assertNotNull(pdf);
    }

    @Test
    void testCreateReserveNonCash() {
        ShoppingDto shoppingDto =
                new ShoppingDto("1", "", new BigDecimal("100.00"), 1, BigDecimal.ZERO,
                        new BigDecimal("100.00"), true);
        ShoppingDto shoppingDto2 =
                new ShoppingDto("1", "", new BigDecimal("100.00"), 1, BigDecimal.ZERO,
                        new BigDecimal("100.00"), false);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto("666666004",
                new BigDecimal("20.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder().path(TicketResource.TICKETS)
                        .body(ticketCreationInputDto).post().build()
        );
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testFindTicketByUserMobile() {
        //Create ticket
        String userMobile = "666666005";
        ShoppingDto shoppingDto = new ShoppingDto("1", "", new BigDecimal("100.00"), 1,
                BigDecimal.ZERO, new BigDecimal("100.00"), true);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(userMobile,
                new BigDecimal("100.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).body(ticketCreationInputDto).post().build();
        //Search setup
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setUserMobile(userMobile);
        //Search
        TicketQueryOutputDto[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TicketQueryOutputDto[]>().clazz(TicketQueryOutputDto[].class))
                .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build();
        assertEquals(2, results.length);
    }

    @Test
    void testFindTicketByUserMobileTicketsNotFoundException() {
        String userMobile = "123456789";
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setUserMobile(userMobile);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder(new RestBuilder<TicketQueryOutputDto[]>()
                        .clazz(TicketQueryOutputDto[].class)).log()
                        .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testFindTicketByUserMobileNotFoundException() {
        String userMobile = "999111222";
        //Search setup
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setUserMobile(userMobile);
        //Generate Exception
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder(new RestBuilder<TicketQueryOutputDto[]>()
                        .clazz(TicketQueryOutputDto[].class)).log()
                        .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testFindTicketByDateRange() {
        LocalDateTime dateStart = LocalDateTime.of(2019, 1, 1, 0, 0, 0);
        LocalDateTime dateEnd = LocalDateTime.of(2019, 1, 10, 0, 0, 0);
        LocalDateTime ticket1Date = LocalDateTime.of(2019, 1, 3, 0, 0, 0);
        LocalDateTime ticket2Date = LocalDateTime.of(2019, 1, 6, 0, 0, 0);
        LocalDateTime ticket3Date = LocalDateTime.of(2019, 1, 25, 0, 0, 0);
        ShoppingDto shoppingDto = new ShoppingDto("1", "", new BigDecimal("100.00"), 1,
                BigDecimal.ZERO, new BigDecimal("100.00"), true);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(null,
                new BigDecimal("100.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        Ticket ticket1 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        Ticket ticket2 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        Ticket ticket3 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticket1.setCreationDate(ticket1Date);
        ticket2.setCreationDate(ticket2Date);
        ticket3.setCreationDate(ticket3Date);
        this.ticketRepository.save(ticket1);
        this.ticketRepository.save(ticket2);
        this.ticketRepository.save(ticket3);
        //Search by Date Range Only
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setDateStart(dateStart);
        searchTicketDto.setDateEnd(dateEnd);
        //Searching
        TicketQueryOutputDto[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TicketQueryOutputDto[]>().clazz(TicketQueryOutputDto[].class))
                .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build();
        assertEquals(2, results.length);
    }

    @Test
    void testFindTicketByDateRangeAndUserMobile() {
        LocalDateTime dateStart = LocalDateTime.of(2019,1,1,0,0,0);
        LocalDateTime dateEnd = LocalDateTime.of(2019,1,10,0,0,0);
        LocalDateTime ticket1Date = LocalDateTime.of(2019,1,3,0,0,0);
        LocalDateTime ticket2Date = LocalDateTime.of(2019,1,6,0,0,0);
        LocalDateTime ticket3Date = LocalDateTime.of(2019,1,25,0,0,0);
        String userMobile1 = "666666005";
        String userMobile2 = "666666004";
        ShoppingDto shoppingDto = new ShoppingDto("1", "", new BigDecimal("100.00"), 1,
                BigDecimal.ZERO, new BigDecimal("100.00"), true);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(userMobile1,
                new BigDecimal("100.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        Ticket ticket1 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        Ticket ticket3 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticketCreationInputDto.setUserMobile(userMobile2);
        Ticket ticket2 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticket1.setCreationDate(ticket1Date);
        ticket2.setCreationDate(ticket2Date);
        ticket3.setCreationDate(ticket3Date);
        this.ticketRepository.save(ticket1);
        this.ticketRepository.save(ticket2);
        this.ticketRepository.save(ticket3);
        //Search by Date Range AND User Mobile
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setUserMobile(userMobile2);
        searchTicketDto.setDateStart(dateStart);
        searchTicketDto.setDateEnd(dateEnd);
        //Searching
        TicketQueryOutputDto[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TicketQueryOutputDto[]>().clazz(TicketQueryOutputDto[].class))
                .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build();
        assertEquals(1, results.length);
    }

    @Test
    void testFindTicketByTotalRange() {
        ShoppingDto shoppingDto = new ShoppingDto("1", "", new BigDecimal("100.00"), 1,
                BigDecimal.ZERO, new BigDecimal("100.00"), true);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto("666666005",
                new BigDecimal("100.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        Ticket ticket1 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticketCreationInputDto = new TicketCreationInputDto("666666005",
                new BigDecimal("200.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto, shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        Ticket ticket2 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticketCreationInputDto = new TicketCreationInputDto("666666005",
                new BigDecimal("300.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto, shoppingDto,
                shoppingDto),"Nota del ticket...", "Nota Regalo...");
        Ticket ticket3 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticketCreationInputDto = new TicketCreationInputDto("666666005",
                new BigDecimal("400.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto, shoppingDto,
                shoppingDto, shoppingDto),"Nota del ticket...", "Nota Regalo...");
        Ticket ticket4 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        this.ticketRepository.save(ticket1);
        this.ticketRepository.save(ticket2);
        this.ticketRepository.save(ticket3);
        this.ticketRepository.save(ticket4);
        //Search by Total Range
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setTotalMin(new BigDecimal("200.00"));
        searchTicketDto.setTotalMax(new BigDecimal("300.00"));
        //Searching
        TicketQueryOutputDto[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TicketQueryOutputDto[]>().clazz(TicketQueryOutputDto[].class))
                .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build();
        assertEquals(2, results.length);
        for(TicketQueryOutputDto result: results){
           // LogManager.getLogger().debug(">>>>> Ticket ID " + result.getId() + " TOTAL: " + result.getTotal());
        }
    }

    @Test
    void testFindTicketByTotalRangeAndUserMobile() {
        String userMobile1 = "666666005";
        String userMobile2 = "666666004";
        ShoppingDto shoppingDto = new ShoppingDto("1", "", new BigDecimal("100.00"), 1,
                BigDecimal.ZERO, new BigDecimal("100.00"), true);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(userMobile1,
                new BigDecimal("100.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        Ticket ticket1 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticketCreationInputDto = new TicketCreationInputDto(userMobile1,
                new BigDecimal("200.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto, shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        Ticket ticket2 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticketCreationInputDto = new TicketCreationInputDto(userMobile2,
                new BigDecimal("300.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto, shoppingDto,
                shoppingDto),"Nota del ticket...", "Nota Regalo...");
        Ticket ticket3 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticketCreationInputDto = new TicketCreationInputDto(userMobile2,
                new BigDecimal("400.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto, shoppingDto,
                shoppingDto, shoppingDto),"Nota del ticket...", "Nota Regalo...");
        Ticket ticket4 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        this.ticketRepository.save(ticket1);
        this.ticketRepository.save(ticket2);
        this.ticketRepository.save(ticket3);
        this.ticketRepository.save(ticket4);
        //Search by Total Range AND User Mobile
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setUserMobile(userMobile1);
        searchTicketDto.setTotalMin(new BigDecimal("200.00"));
        searchTicketDto.setTotalMax(new BigDecimal("300.00"));
        //Searching
        TicketQueryOutputDto[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TicketQueryOutputDto[]>().clazz(TicketQueryOutputDto[].class))
                .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build();
        assertEquals(1, results.length);
        //for(TicketQueryOutputDto result: results){
            //LogManager.getLogger().debug(">>>>> Ticket ID " + result.getId() + " TOTAL: " + result.getTotal());
        //}
    }

    @Test
    void testFindByTotalRangeAndDateRange() {
        LocalDateTime dateStart = LocalDateTime.of(2019, 1, 1, 0, 0, 0);
        LocalDateTime dateEnd = LocalDateTime.of(2019, 1, 10, 0, 0, 0);
        LocalDateTime ticket1Date = LocalDateTime.of(2019, 1, 3, 0, 0, 0);
        LocalDateTime ticket2Date = LocalDateTime.of(2019, 1, 6, 0, 0, 0);
        LocalDateTime ticket3Date = LocalDateTime.of(2019, 1, 25, 0, 0, 0);
        String userMobile1 = "666666005";
        String userMobile2 = "666666004";
        ShoppingDto shoppingDto = new ShoppingDto("1", "", new BigDecimal("100.00"), 1,
                BigDecimal.ZERO, new BigDecimal("100.00"), true);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(userMobile1,
                new BigDecimal("100.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        Ticket ticket1 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticketCreationInputDto = new TicketCreationInputDto(userMobile1,
                new BigDecimal("200.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto, shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        Ticket ticket2 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticketCreationInputDto = new TicketCreationInputDto(userMobile2,
                new BigDecimal("300.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto, shoppingDto,
                shoppingDto),"Nota del ticket...", "Nota Regalo...");
        Ticket ticket3 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticketCreationInputDto = new TicketCreationInputDto(userMobile2,
                new BigDecimal("400.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto, shoppingDto,
                shoppingDto, shoppingDto),"Nota del ticket...", "Nota Regalo...");
        Ticket ticket4 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticket1.setCreationDate(ticket1Date);
        ticket2.setCreationDate(ticket2Date);
        ticket3.setCreationDate(ticket2Date);
        ticket4.setCreationDate(ticket3Date);
        this.ticketRepository.save(ticket1);
        this.ticketRepository.save(ticket2);
        this.ticketRepository.save(ticket3);
        this.ticketRepository.save(ticket4);
        //Search by Total Range AND Date Range
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setDateStart(dateStart);
        searchTicketDto.setDateEnd(dateEnd);
        searchTicketDto.setTotalMin(new BigDecimal("200.00"));
        searchTicketDto.setTotalMax(new BigDecimal("300.00"));
        //Searching
        TicketQueryOutputDto[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TicketQueryOutputDto[]>().clazz(TicketQueryOutputDto[].class))
                .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build();
        assertEquals(2, results.length);
        for(TicketQueryOutputDto result: results){
            //LogManager.getLogger().debug(">>>>> Ticket ID " + result.getId() + " TOTAL: " + result.getTotal());
        }
    }

    @Test
    void testFindByTotalRangeAndDateRangeAndUserMobile() {
        LocalDateTime dateStart = LocalDateTime.of(2019, 1, 1, 0, 0, 0);
        LocalDateTime dateEnd = LocalDateTime.of(2019, 1, 10, 0, 0, 0);
        LocalDateTime ticket1Date = LocalDateTime.of(2019, 1, 3, 0, 0, 0);
        LocalDateTime ticket2Date = LocalDateTime.of(2019, 1, 6, 0, 0, 0);
        LocalDateTime ticket3Date = LocalDateTime.of(2019, 1, 25, 0, 0, 0);
        String userMobile1 = "666666005";
        String userMobile2 = "666666004";
        ShoppingDto shoppingDto = new ShoppingDto("1", "", new BigDecimal("100.00"), 1,
                BigDecimal.ZERO, new BigDecimal("100.00"), true);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(userMobile1,
                new BigDecimal("100.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        Ticket ticket1 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticketCreationInputDto = new TicketCreationInputDto(userMobile1,
                new BigDecimal("200.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto, shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        Ticket ticket2 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticketCreationInputDto = new TicketCreationInputDto(userMobile2,
                new BigDecimal("300.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto, shoppingDto,
                shoppingDto),"Nota del ticket...", "Nota Regalo...");
        Ticket ticket3 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticketCreationInputDto = new TicketCreationInputDto(userMobile2,
                new BigDecimal("400.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto, shoppingDto,
                shoppingDto, shoppingDto),"Nota del ticket...", "Nota Regalo...");
        Ticket ticket4 = this.ticketController.createTicketForTests(ticketCreationInputDto);
        ticket1.setCreationDate(ticket1Date);
        ticket2.setCreationDate(ticket2Date);
        ticket3.setCreationDate(ticket2Date);
        ticket4.setCreationDate(ticket3Date);
        this.ticketRepository.save(ticket1);
        this.ticketRepository.save(ticket2);
        this.ticketRepository.save(ticket3);
        this.ticketRepository.save(ticket4);
        //Search by Total Range AND Date Range
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setUserMobile(userMobile1);
        searchTicketDto.setDateStart(dateStart);
        searchTicketDto.setDateEnd(dateEnd);
        searchTicketDto.setTotalMin(new BigDecimal("200.00"));
        searchTicketDto.setTotalMax(new BigDecimal("300.00"));
        //Searching
        TicketQueryOutputDto[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TicketQueryOutputDto[]>().clazz(TicketQueryOutputDto[].class))
                .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build();
        assertEquals(1, results.length);
        for(TicketQueryOutputDto result: results){
            //LogManager.getLogger().debug(">>>>> Ticket ID " + result.getId() + " TOTAL: " + result.getTotal());
        }
    }

    @Test
    void testFindByPendingStatus() {
        this.ticketRepository.deleteAll();
        String userMobile = "666666004";
        ShoppingDto shoppingDto =
                new ShoppingDto("1", "", new BigDecimal("100.00"), 1, BigDecimal.ZERO,
                        new BigDecimal("100.00"), false);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(userMobile,
                BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, Arrays.asList(shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).body(ticketCreationInputDto).post().build();
        this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).body(ticketCreationInputDto).post().build();
        shoppingDto =
                new ShoppingDto("1", "", new BigDecimal("200.00"), 1, BigDecimal.ZERO,
                        new BigDecimal("200.00"), true);
        ticketCreationInputDto = new TicketCreationInputDto(userMobile,
                new BigDecimal("200.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).body(ticketCreationInputDto).post().build();
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setPending(true);
        TicketQueryOutputDto[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TicketQueryOutputDto[]>().clazz(TicketQueryOutputDto[].class))
                .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build();
        assertEquals(2, results.length);
    }

    @Test
    void testFindByPendingAndByUserMobileAndByTotalRange() {
        this.ticketRepository.deleteAll();
        String userMobile1 = "666666005";
        String userMobile2 = "666666004";
        ShoppingDto shoppingDto = new ShoppingDto("1", "", new BigDecimal("100.00"), 1,
                BigDecimal.ZERO, new BigDecimal("100.00"), false);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(userMobile1,
                new BigDecimal("100.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).body(ticketCreationInputDto).post().build();
        ticketCreationInputDto = new TicketCreationInputDto(userMobile2,
                new BigDecimal("200.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto, shoppingDto),
                "Nota del ticket...", "Nota Regalo...");
        this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).body(ticketCreationInputDto).post().build();
        //Search by Total Range AND User Mobile
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setPending(true);
        searchTicketDto.setUserMobile(userMobile1);
        searchTicketDto.setTotalMin(new BigDecimal("100.00"));
        searchTicketDto.setTotalMax(new BigDecimal("200.00"));
        //Searching
        TicketQueryOutputDto[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TicketQueryOutputDto[]>().clazz(TicketQueryOutputDto[].class))
                .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build();
        assertEquals(1, results.length);
        for(TicketQueryOutputDto result: results){
            //LogManager.getLogger().debug(">>>>> Ticket ID " + result.getId() + " TOTAL: " + result.getTotal());
        }
    }

    @Test
    void testAdvancedTicketQueryByOrderId() {
        //Setup Order
        String articleId = "8400000000017";
        Article article = this.articleRepository.findById(articleId).orElse(null);
        OrderLine orderLine = new OrderLine(article, 10);
        OrderLine[] orderLines = {orderLine};
        Order order = new Order("ORDER-" + String.valueOf((int) (Math.random() * 10000)), null, orderLines);
        this.orderRepository.save(order);
        //Setup Ticket
        this.ticketRepository.deleteAll();
        String userMobile1 = "666666005";
        ShoppingDto shoppingDto1 = new ShoppingDto("1", "", new BigDecimal("100.00"), 1,
                BigDecimal.ZERO, new BigDecimal("100.00"), true);
        ShoppingDto shoppingDto2 = new ShoppingDto(articleId, "", new BigDecimal("27.80"), 1,
                BigDecimal.ZERO, new BigDecimal("27.80"), false);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(userMobile1,
                new BigDecimal("127.80"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto1, shoppingDto2),
                "Nota del ticket...", "Nota Regalo...");
        this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).body(ticketCreationInputDto).post().build();
        ShoppingDto shoppingDto3 = new ShoppingDto(articleId, "", new BigDecimal("27.80"), 1,
                BigDecimal.ZERO, new BigDecimal("27.80"), true);
        ticketCreationInputDto = new TicketCreationInputDto(userMobile1,
                new BigDecimal("27.80"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto3),
                "Nota del ticket...", "Nota Regalo...");
        this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).body(ticketCreationInputDto).post().build();
        //Setup Search
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setPending(true);
        searchTicketDto.setOrderId(order.getId());
        //Searching
        TicketQueryOutputDto[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TicketQueryOutputDto[]>().clazz(TicketQueryOutputDto[].class))
                .path(TicketResource.TICKETS).path(TicketResource.QUERY).path(TicketResource.ORDER_ID)
                .body(searchTicketDto).post().build();
        assertEquals(1, results.length);
    }

    @Test
    void testAdvancedTicketQueryByOrderIdNotFoundException() {
        String orderId = "888666";
        //Search setup
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setOrderId(orderId);
        //Generate Exception
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder(new RestBuilder<TicketQueryOutputDto[]>()
                        .clazz(TicketQueryOutputDto[].class)).log()
                        .path(TicketResource.TICKETS).path(TicketResource.QUERY).path(TicketResource.ORDER_ID)
                        .body(searchTicketDto).post().build());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testObtainTicketModifiedById() {
        TicketModificationStateOrAmountDto modifiedTicket = restService.loginAdmin()
                .restBuilder(new RestBuilder<TicketModificationStateOrAmountDto>())
                .clazz(TicketModificationStateOrAmountDto.class)
                .path(TicketResource.TICKETS).path(TicketResource.TICKET_ID).expand("201901121")
                .get().build();
        assertNotNull(modifiedTicket);
    }

    @Test
    void testUpdateModifiedTicketAndPdf() {
        Ticket ticket = ticketController.readTicketById("201901121");
        TicketModificationStateOrAmountDto modifiedTicket = new TicketModificationStateOrAmountDto(ticket, false);
        byte[] pdf = restService.loginAdmin()
                .restBuilder(new RestBuilder<byte[]>())
                .clazz(byte[].class)
                .path(TicketResource.TICKETS).path(TicketResource.TICKET_ID)
                .body(modifiedTicket)
                .expand("201901121")
                .put().build();
        assertNotNull(pdf);
    }

    @Test
    void testGetDateSold() {
        List<Article> articles = Arrays.asList(this.restService.loginAdmin().restBuilder(new RestBuilder<Article[]>())
                .clazz(Article[].class).path(TicketResource.TICKETS).path(TicketResource.DATE_SOLD).expand("2019-05-30T00:00:00")
                .get().build());
        assertNotNull(articles);
        assertTrue(articles.size() > 1);
    }

}
