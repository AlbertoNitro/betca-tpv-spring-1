package es.upm.miw.rest_controllers;

import es.upm.miw.dtos.*;
import es.upm.miw.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
class TicketResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateTicket() {
        ShoppingDto shoppingDto =
                new ShoppingDto("1", "", new BigDecimal("100.00"), 1, BigDecimal.ZERO,
                        new BigDecimal("100.00"), true);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(null, new BigDecimal("100.00")
                , BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto), "Nota del ticket...");
        byte[] pdf = this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).body(ticketCreationInputDto)
                .post().build();
        assertNotNull(pdf);
    }

    @Test
    void testCreateReserve() {
        ShoppingDto shoppingDto =
                new ShoppingDto("1", "", new BigDecimal("100.00"), 1, BigDecimal.ZERO,
                        new BigDecimal("100.00"), false);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto("666666004",
                BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, Arrays.asList(shoppingDto),
                "Nota del ticket...");
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
                "Nota del ticket...");
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder().path(TicketResource.TICKETS)
                        .body(ticketCreationInputDto).post().build()
        );
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testFindTicketByUserMobile() {
        //Crear ticket
        String userMobile = "666666005";
        ShoppingDto shoppingDto = new ShoppingDto("1", "", new BigDecimal("100.00"), 1,
                BigDecimal.ZERO, new BigDecimal("100.00"), true);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(userMobile,
                new BigDecimal("100.00"), BigDecimal.ZERO, BigDecimal.ZERO, Arrays.asList(shoppingDto),
                "Nota del ticket...");
        this.restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).clazz(byte[].class)
                .path(TicketResource.TICKETS).body(ticketCreationInputDto).post().build();
        //Preparacion de busqueda
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setUserMobile(userMobile);
        //Busqueda
        TicketQueryResultDto[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<TicketQueryResultDto[]>().clazz(TicketQueryResultDto[].class))
                .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build();
        assertEquals(1, results.length);
    }

    @Test
    void testFindTicketByUserMobileTicketsNotFoundException() {
        //Crear ticket
        String userMobile = "666666005";
        //Preparacion de busqueda
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setUserMobile(userMobile);
        //Generar excepcion
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder(new RestBuilder<TicketQueryResultDto[]>()
                        .clazz(TicketQueryResultDto[].class)).log()
                        .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testFindTicketByUserMobileNotFoundException() {
        String userMobile = "999111222";
        //Preparacion de busqueda
        TicketQueryInputDto searchTicketDto = new TicketQueryInputDto();
        searchTicketDto.setUserMobile(userMobile);
        //Generar excepcion
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                this.restService.loginAdmin().restBuilder(new RestBuilder<TicketQueryResultDto[]>()
                        .clazz(TicketQueryResultDto[].class)).log()
                        .path(TicketResource.TICKETS).path(TicketResource.QUERY).body(searchTicketDto).post().build());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
