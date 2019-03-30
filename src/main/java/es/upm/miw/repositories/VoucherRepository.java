package es.upm.miw.repositories;

import es.upm.miw.documents.Voucher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface VoucherRepository extends MongoRepository<Voucher, String> {
    @Query(value = "{}", fields = "{ '_id' : 1, 'value' : 1,'dateOfUse' : 1}")
    List<Voucher> findAllVouchers();

    List<Voucher> findByCreationDateBetweenAndDateOfUseIsNull(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Voucher> findByCreationDateBetweenAndDateOfUseIsNotNull(LocalDateTime dateFrom, LocalDateTime dateTo);

}
