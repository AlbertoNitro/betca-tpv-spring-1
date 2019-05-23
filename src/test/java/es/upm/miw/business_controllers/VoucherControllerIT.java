package es.upm.miw.business_controllers;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.Voucher;
import es.upm.miw.dtos.input.VoucherInputDto;
import es.upm.miw.dtos.output.VoucherOutputDto;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.VoucherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class VoucherControllerIT {

    @Autowired
    private VoucherController voucherController;

    @Autowired
    private VoucherRepository voucherRepository;

    private VoucherOutputDto voucherOutputDto;
    private Voucher voucher;

    @BeforeEach
    void seed() {
        this.voucherOutputDto = new VoucherOutputDto(BigDecimal.TEN, LocalDateTime.now(), null);
        this.voucher = new Voucher();
        this.voucher.setId("99999999");
        this.voucher.setValue(BigDecimal.TEN);
        this.voucher.use();
        this.voucherRepository.save(this.voucher);
    }

    @Test
    void testCreateVoucher() {
        VoucherInputDto voucherInputDto = new VoucherInputDto(BigDecimal.TEN);
        VoucherOutputDto voucherOutputDto = voucherController.create(voucherInputDto);
        assertEquals(voucherOutputDto.getValue(), voucherInputDto.getValue());
    }

    @Test
    void testReadAllVouchers() {
        List<VoucherOutputDto> vouchers = voucherController.readAll();
        assertTrue(vouchers.size() > 0);
    }

    @Test
    void testUpdateVoucher() {
        VoucherOutputDto voucherResult = this.voucherController.update("0123456789");
        assertNotNull(voucherResult.getDateOfUse());
    }

    @Test
    void testUpdateVoucherNotFoundException() {
        assertThrows(NotFoundException.class, () -> this.voucherController.update("123"));
    }

    @Test
    void testFindVouchersByDateConsumed() {
        LocalDateTime dateFrom = LocalDateTime.now().minusDays(1).withNano(0);
        LocalDateTime dateTo = LocalDateTime.now().plusDays(1).withNano(0);
        List<VoucherOutputDto> voucherDtoOutputList = this.voucherController
                .findVouchersByDateConsumed(dateFrom.toString(), dateTo.toString());
        assertTrue(voucherDtoOutputList.size() >= 1);
    }

    @Test
    void testFindVouchersByDateWithinConsumed() {
        LocalDateTime dateFrom = LocalDateTime.now().minusDays(10).withNano(0);
        LocalDateTime dateTo = LocalDateTime.now().plusDays(10).withNano(0);
        List<VoucherOutputDto> voucherDtoOutputList = this.voucherController
                .findVouchersByDateWithinConsumed(dateFrom.toString(), dateTo.toString());
        assertTrue(voucherDtoOutputList.size() > 0);
    }

    @Test
    void testReadById() {
        VoucherOutputDto voucherdto = voucherController.readById(this.voucher.getId());
        assertNotNull(voucherdto);
        assertNotNull(voucherdto.getId());
    }


}