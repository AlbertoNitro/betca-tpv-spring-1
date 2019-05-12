package es.upm.miw.repositories;

import es.upm.miw.documents.Invoice;
import es.upm.miw.dtos.ProviderMinimunDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {
}
