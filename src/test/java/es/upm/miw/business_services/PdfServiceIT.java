package es.upm.miw.business_services;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.RgpdAgreementType;
import es.upm.miw.documents.User;
import es.upm.miw.repositories.TicketRepository;
import es.upm.miw.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
public class PdfServiceIT {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void testPdfGenerateTicket() {
        assertNotNull(this.pdfService.generateTicket(this.ticketRepository.findById("201901121").get()));
    }

    @Test
    void testPdfGenerateGiftTicket() {
        assertNotNull(this.pdfService.generateGiftTicket(this.ticketRepository.findById("201901121").get()));
    }

    @Test
    void testPdfGeneratePrintableRgpdAgreement() {
        User user = new User("999777666", "123445", "666001110", "123445",
                "C/ TPV, 100, 1A, 28000 Madrid", "user2@gmail.com");
        assertNotNull(this.pdfService.generatePrintableRgpdAgreement(user, RgpdAgreementType.MEDIUM));
    }
}
