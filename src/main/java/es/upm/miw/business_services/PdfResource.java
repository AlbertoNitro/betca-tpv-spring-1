package es.upm.miw.business_services;

import es.upm.miw.exceptions.PdfException;
import org.springframework.web.bind.annotation.RequestParam;

public class PdfResource {

    public static final String PDFS = "/tpv/pdfs";

    public byte[] pdf(@RequestParam String title, @RequestParam String paragraph) throws PdfException {
        return new PdfBuilder(PDFS)
                .paragraphEmphasized(title).line()
                .paragraph(paragraph)
                .paragraph("Lorem ipsum dolor sit amet, sea ea dico suas iracundia, in has deserunt mediocritatem. "
                        + "Altera qualisque eum at, eam id animal appareat, veri prompta duo ne. Choro civibus ex vim,"
                        + " ei nam brute graecis, quo ea inimicus interpretaris.").line()
                .barCode("8015187008499").line()
                .qrCode("BETCA: Spring").line()
                .image("logoMiw.png").line()
                .table(15, 90, 15, 25, 35, 15)
                .tableCell("1", "2", "3", "4", "5", "6")
                .tableCell("1", "2", "3", "4", "5", "6")
                .tableCell("1", "2", "3", "4", "5", "6")
                .tableColumnsHeader(" ", "Desc.", "Ud.", "Dto.%", "€", "E.")
                .tableColspanRight("TOTAL")
                .closeTable().line()
                .build();
    }
}
