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
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

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
        assertTrue(code.equals(result.getId()));

    }


    private RestBuilder<VoucherOutputDto> restUpdateBuilder(String id) {
        return this.restService.loginAdmin()
                .restBuilder(new RestBuilder<VoucherOutputDto>()).clazz(VoucherOutputDto.class)
                .path(VoucherResource.VOUCHERS).path("/" + id)
                .put();
    }

    private RestBuilder<VoucherOutputDto> restCreateService(VoucherOutputDto voucherDto) {
        return this.restService.loginAdmin()
                .restBuilder(new RestBuilder<VoucherOutputDto>()).clazz(VoucherOutputDto.class)
                .path(VoucherResource.VOUCHERS)
                .body(voucherDto)
                .post();
    }



}
