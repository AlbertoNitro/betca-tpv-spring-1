package es.upm.miw.repositories;

import es.upm.miw.documents.Voucher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VoucherRepository extends MongoRepository<Voucher, String> {
    @Query(value = "{}", fields = "{ '_id' : 1, 'value' : 1,'dateOfUse' : 1}")
    List<Voucher> findAllVouchers();

}
