package es.upm.miw.repositories;

import es.upm.miw.TestConfig;
import es.upm.miw.business_services.Encrypting;
import es.upm.miw.documents.Voucher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class VoucherRepositoryIT {

    @Autowired
    private VoucherRepository voucherRepository;

    private Voucher voucher;

    @BeforeEach
    void seedDb() {
        String id;
        do {
            id = new Encrypting().shortId64UrlSafe();
        } while (this.voucherRepository.existsById(id));
        this.voucher = new Voucher(id, BigDecimal.TEN);
        voucherRepository.save(this.voucher);
    }

    @Test
    void testFindById() {
        Voucher voucher = voucherRepository.findById(this.voucher.getId()).get();
        assertFalse(voucher.isUsed());
    }

    @AfterEach
    void delete() {
        this.voucherRepository.delete(this.voucher);
    }

    @Test
    void findByCreationDateBetweenAndDateOfUseIsNull() {
        LocalDateTime dateFrom = LocalDateTime.of(2019, 3, 1, 0, 0, 0);
        LocalDateTime dateTo = LocalDateTime.of(2019, 4, 30, 23, 59, 59);
        List<Voucher> lvouc = voucherRepository.findByCreationDateBetweenAndDateOfUseIsNull(dateFrom, dateTo);
        Voucher vouch = lvouc.get(0);
        vouch.use();
        voucherRepository.save(vouch);
        List<Voucher> lvouc2 = voucherRepository.findByCreationDateBetweenAndDateOfUseIsNull(dateFrom, dateTo);
        assertFalse(lvouc.isEmpty());
        assertTrue(lvouc.size() != lvouc2.size());
    }

    @Test
    void findByCreationDateBetweenAndDateOfUseIsNotNull() {
        LocalDateTime dateFrom = LocalDateTime.of(2019, 3, 1, 0, 0, 0);
        LocalDateTime dateTo = LocalDateTime.of(2019, 4, 30, 23, 59, 59);
        List<Voucher> lvouc = voucherRepository.findByCreationDateBetweenAndDateOfUseIsNotNull(dateFrom, dateTo);
        assertFalse(lvouc.isEmpty());
        Voucher vouch = new Voucher();
        vouch.use();
        voucherRepository.save(vouch);
        List<Voucher> lvouc2 = voucherRepository.findByCreationDateBetweenAndDateOfUseIsNotNull(dateFrom, dateTo);
        assertTrue(lvouc.size() != lvouc2.size());
    }

    @Test
    void findById() {
        List<Voucher> lvouchers = voucherRepository.findAll();
        Voucher firstVoucher = lvouchers.get(0);
        Optional<Voucher> voucherBBDD = voucherRepository.findById(firstVoucher.getId());
        assertTrue(voucherBBDD.isPresent());
    }

}
