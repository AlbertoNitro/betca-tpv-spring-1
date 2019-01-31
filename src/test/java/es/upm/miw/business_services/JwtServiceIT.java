package es.upm.miw.business_services;

import es.upm.miw.TestConfig;
import es.upm.miw.business_controllers.ArticleController;
import es.upm.miw.dtos.ArticleDto;
import es.upm.miw.exceptions.ConflictRequestException;
import es.upm.miw.exceptions.JwtException;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestConfig
class JwtServiceIT {

    @Autowired
    private JwtService jwtService;


    @BeforeEach
    void seed() {
    }

    @Test
    void testJwtExceptionNotBearer() {
        assertThrows(JwtException.class, () -> this.jwtService.user("Not Bearer"));
    }

    @Test
    void testJwtExceptionTokenError() {
        assertThrows(JwtException.class, () -> this.jwtService.user("Bearer error.error.error"));
    }

}
