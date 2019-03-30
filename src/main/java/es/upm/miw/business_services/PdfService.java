package es.upm.miw.business_services;

import es.upm.miw.documents.Budget;
import es.upm.miw.documents.Shopping;
import es.upm.miw.documents.ShoppingState;
import es.upm.miw.documents.Ticket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    private static final String[] TABLE_COLUMNS_HEADERS = {" ", "Desc.", "Ud.", "Dto.%", "€", "E."};
    private static final float[] TABLE_COLUMNS_SIZES_TICKETS = {15, 90, 15, 25, 35, 15};

    @Value("${miw.company.logo}")
    private String logo;

    @Value("${miw.company.name}")
    private String name;

    @Value("${miw.company.nif}")
    private String nif;

    @Value("${miw.company.phone}")
    private String phone;

    @Value("${miw.company.address}")
    private String address;

    @Value("${miw.company.email}")
    private String email;

    @Value("${miw.company.web}")
    private String web;

    private void generateCommonHead(PdfBuilder pdf) {
        pdf.image(this.logo).paragraphEmphasized(this.name).paragraphEmphasized("Tfn: " + this.phone)
                .paragraph("NIF: " + this.nif + "   -   " + this.address)
                .paragraph("Email: " + this.email + "  -  " + "Web: " + this.web);
        pdf.line();
    }

    private void generateCommonFooter(PdfBuilder pdf) {
        pdf.paragraphEmphasized("Gracias por su visita").paragraphEmphasized(" ").line();
    }

    private int generateTableCellFromShopping(PdfTableBuilder table, Shopping shopping, int index, int notCommitted) {
        String state = "";
        if (shopping.getShoppingState() != ShoppingState.COMMITTED && shopping.getAmount() > 0) {
            state = "N";
            notCommitted++;
        }
        String discount = "";
        if ((shopping.getDiscount().doubleValue() > 0.009) && !shopping.getArticle().getCode().equals("1")) {
            discount = "" + shopping.getDiscount().setScale(2, RoundingMode.HALF_UP);
        }
        table.tableCell(String.valueOf(index + 1), shopping.getDescription(), "" + shopping.getAmount(), discount,
                shopping.getShoppingTotal().setScale(2, RoundingMode.HALF_UP) + "€", state);

        return notCommitted;
    }

    public byte[] generateBudget(Budget budget) {
        final String path = "/tpv-pdfs/budgets/budget-" + budget.getId();
        PdfBuilder pdf = new PdfBuilder(path, "");
        this.generateCommonHead(pdf);
        pdf.paragraphEmphasized("PRESUPUESTO");
        pdf.line();
        pdf.paragraphEmphasized(budget.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        PdfTableBuilder table = pdf.table(TABLE_COLUMNS_SIZES_TICKETS).tableColumnsHeader(TABLE_COLUMNS_HEADERS);
        for (int i = 0; i < budget.getShoppingList().length; i++) {
            Shopping shopping = budget.getShoppingList()[i];
            this.generateTableCellFromShopping(table, shopping, i, 0);
        }
        table.build();
        pdf.paragraph("ID PRESUPUESTO: " + budget.getId());
        pdf.line();
        this.generateCommonFooter(pdf);

        return pdf.build();
    }

    public byte[] generateTicket(Ticket ticket) {
        final String path = "/tpv-pdfs/tickets/ticket-" + ticket.getId();
        PdfBuilder pdf = new PdfBuilder(path, "");
        this.generateCommonHead(pdf);
        if (ticket.isDebt()) {
            pdf.paragraphEmphasized("RESERVA");
            pdf.paragraphEmphasized("Abonado: " + ticket.pay().setScale(2, RoundingMode.HALF_UP) + "€");
            pdf.paragraphEmphasized("Pendiente: " + ticket.debt().setScale(2, RoundingMode.HALF_UP) + "€");
        } else {
            pdf.paragraphEmphasized("TICKET");
        }
        pdf.barCode(ticket.getId());
        pdf.line();
        pdf.paragraphEmphasized(ticket.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        int notCommitted = 0;
        PdfTableBuilder table = pdf.table(TABLE_COLUMNS_SIZES_TICKETS).tableColumnsHeader(TABLE_COLUMNS_HEADERS);
        for (int i = 0; i < ticket.getShoppingList().length; i++) {
            Shopping shopping = ticket.getShoppingList()[i];
            notCommitted = this.generateTableCellFromShopping(table, shopping, i, notCommitted);
        }
        table.tableColspanRight(ticket.getTotal().setScale(2, RoundingMode.HALF_UP) + "€").build();
        pdf.paragraph(ticket.getNote());
        if (notCommitted > 0) {
            pdf.paragraphEmphasized("Artículos pendientes de entrega: " + notCommitted);
            if (ticket.getUser() != null) {
                pdf.paragraph("Teléfono de contacto: " + ticket.getUser().getMobile() + " - " + ticket.getUser().getUsername().substring(0,
                        (ticket.getUser().getUsername().length() > 10) ? 10 : ticket.getUser().getUsername().length()));
            }
            pdf.qrCode(ticket.getReference());
        }
        pdf.line().paragraph("Periodo de devolución o cambio: 15 dias a partir de la fecha del ticket");
        this.generateCommonFooter(pdf);
        return pdf.build();
    }

    public byte[] generatePrintableRgpdAgreement(String userName) {
        final String path = "/rgpd-pdfs/printable/agreement-" + userName;
        PdfBuilder pdf = new PdfBuilder(path, "");
        pdf.image(this.logo).paragraphEmphasized(this.name).paragraphEmphasized("Tfn: " + this.phone)
                .paragraph("NIF: " + this.nif + "   -   " + this.address)
                .paragraph("Email: " + this.email + "  -  " + "Web: " + this.web);
        pdf.line();

        pdf.paragraphEmphasized("Contrato de proteccion de datos.");
        pdf.paragraphEmphasized(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        pdf.line();
        pdf.paragraphEmphasized("Otorgante: " + userName);
        pdf.line();
        pdf.paragraphEmphasized("Tal: ");
        pdf.paragraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse mollis neque commodo urna tristique rhoncus. Etiam varius leo et velit posuere, a molestie ex consectetur. Duis sit amet nulla in massa sagittis porttitor commodo vitae quam. Vivamus viverra libero id risus porttitor bibendum. Morbi et facilisis sapien. Nullam vitae rhoncus ligula. Cras accumsan cursus ipsum, vitae commodo quam ullamcorper in. Ut tincidunt mi eget lectus mattis bibendum.");
        pdf.paragraph("Quisque vulputate nunc sit amet ligula pretium malesuada. Fusce imperdiet ante id elementum eleifend. Suspendisse massa libero, egestas interdum imperdiet quis, lacinia ut elit. Etiam velit nisi, placerat sit amet facilisis eget, euismod a ipsum. Fusce malesuada quis justo ac ultrices. Nunc quis rhoncus neque. Integer condimentum, leo aliquet pulvinar molestie, tortor nulla consectetur elit, a rhoncus risus sapien in augue. Mauris vel nisl eu risus varius cursus et vitae lorem. Donec molestie venenatis feugiat. Proin quis turpis leo. Nulla eu dolor vitae arcu suscipit tincidunt eget id arcu. Ut dictum nisi in risus porta, nec maximus felis scelerisque. In aliquet diam nibh, ac pulvinar arcu eleifend vel. Sed in convallis nulla. Donec fermentum, sapien at facilisis lacinia, neque sapien condimentum nunc, ut efficitur est lectus a arcu.");

        pdf.paragraph("Etiam est felis, tristique ut tristique id, dapibus a orci. Mauris rutrum velit at lectus tristique, a congue ex lobortis. Praesent orci nulla, scelerisque ut maximus quis, ullamcorper non leo. Integer laoreet ex vel odio dignissim lobortis. In at lorem dolor. Maecenas quis diam nec leo consectetur commodo in in enim. Mauris sit amet ligula vitae magna tincidunt sagittis. Quisque ac massa nibh. Proin hendrerit rutrum augue et lacinia. Nam ullamcorper mi a felis euismod malesuada. Cras pharetra ligula a tempor porta. Nunc sed volutpat purus. Curabitur eget accumsan arcu. Integer lobortis sem libero, luctus interdum sapien cursus sed. Duis quis rhoncus nibh, eu gravida massa. Proin vel neque egestas erat pellentesque ultricies.");

        pdf.paragraph("Curabitur commodo metus a vestibulum gravida. Nunc efficitur aliquam tristique. Maecenas accumsan erat nibh, et elementum risus dictum posuere. Sed ut mauris vel ligula pretium molestie. Proin fringilla ut nunc sit amet viverra. Suspendisse tristique augue sed dui congue pulvinar. Etiam ex tortor, vulputate at faucibus sit amet, egestas id libero. Praesent sodales vel ante at ultrices. Cras condimentum convallis arcu, vel tincidunt neque tincidunt id. Fusce nisl tortor, posuere facilisis lectus a, mollis viverra massa.");

        pdf.paragraph("Morbi lorem elit, fringilla sed lorem quis, tempor tempus sapien. Mauris placerat justo ultrices, porta diam venenatis, laoreet orci. Cras elementum mi ut lacus elementum, eget sodales turpis pellentesque. Fusce quis consectetur justo. Curabitur quis lobortis risus. Duis in eros vel lorem tempus elementum. Vestibulum orci orci, semper in urna iaculis, tincidunt semper orci. Phasellus non rhoncus odio. Nullam nibh urna, porttitor a mattis nec, tincidunt eget orci. Quisque pharetra hendrerit ante. In nunc ex, suscipit id placerat vitae, faucibus ut orci.");
        pdf.paragraphEmphasized("Gracias por su visita").paragraphEmphasized(" ").line();
        return pdf.build();
    }
}
