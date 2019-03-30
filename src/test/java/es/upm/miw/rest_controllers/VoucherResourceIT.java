package es.upm.miw.rest_controllers;


import es.upm.miw.business_controllers.VoucherController;
import es.upm.miw.documents.Voucher;
import es.upm.miw.dtos.input.VoucherInputDto;
import es.upm.miw.dtos.output.VoucherOutputDto;
import es.upm.miw.repositories.VoucherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ApiTestConfig
class VoucherResourceIT {
    @Autowired
    private RestService restService;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherController voucherController;

    private List<Voucher> initialVoucherDB;

    @BeforeEach
    void backupVoucherDB() {
        initialVoucherDB = this.voucherRepository.findAll();
    }

    @AfterEach
    void resetTicketDB() {
        this.voucherRepository.deleteAll();
        this.voucherRepository.saveAll(this.initialVoucherDB);
    }

    @Test
    void testCreateVoucher() {
        Voucher voucher = new Voucher();
        voucher.setValue(BigDecimal.TEN);
        VoucherInputDto voucherInputDto = new VoucherInputDto(voucher.getValue());
        VoucherOutputDto voucherDto = this.restService.loginAdmin().restBuilder(new RestBuilder<VoucherOutputDto>())
                .clazz(VoucherOutputDto.class).path(VoucherResource.VOUCHERS).body(voucherInputDto).post().build();
        assertNotNull(voucherDto);
        assertNotNull(voucherDto.getId());
    }

    @Test
    void testReadAll() {
        List<VoucherOutputDto> voucherDtoList = Arrays.asList(this.restService.loginAdmin()
                .restBuilder(new RestBuilder<VoucherOutputDto[]>()).clazz(VoucherOutputDto[].class)
                .path(VoucherResource.VOUCHERS)
                .get().build());
        assertTrue(voucherDtoList.size() > 0);
    }

    @Test
    void testUpdate() {
        String code = "1234567890";
        VoucherOutputDto result = restUpdateBuilder(code).build();
        Assertions.assertEquals(code, result.getId());

    }


    private RestBuilder<VoucherOutputDto> restUpdateBuilder(String id) {
        return this.restService.loginAdmin()
                .restBuilder(new RestBuilder<VoucherOutputDto>()).clazz(VoucherOutputDto.class)
                .path(VoucherResource.VOUCHERS).path("/" + id)
                .put();
    }


    @Test
    void testSearchVoucherConsumed() {
        LocalDateTime dateFrom = LocalDateTime.of(2019, 3, 1, 0, 0, 0);
        LocalDateTime dateTo = LocalDateTime.of(2019, 3, 31, 23, 59, 59);
        VoucherOutputDto[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<VoucherOutputDto[]>().clazz(VoucherOutputDto[].class))
                .path(VoucherResource.VOUCHERS)
                .path(VoucherResource.SEARCH)
                .path("?consumed=true&dateFrom=" + dateFrom + "&dateTo=" + dateTo).get().build();
        assertTrue(results.length >= 0);
    }

    @Test
    void testSearchVoucherWithinConsumed() {
        LocalDateTime dateFrom = LocalDateTime.of(2019, 3, 1, 0, 0, 0);
        LocalDateTime dateTo = LocalDateTime.of(2019, 3, 31, 23, 59, 59);
        VoucherOutputDto[] results = this.restService.loginAdmin()
                .restBuilder(new RestBuilder<VoucherOutputDto[]>().clazz(VoucherOutputDto[].class))
                .path(VoucherResource.VOUCHERS)
                .path(VoucherResource.SEARCH)
                .path("?consumed=false&dateFrom=" + dateFrom + "&dateTo=" + dateTo).get().build();
        assertTrue(results.length >= 0);
    }

}
