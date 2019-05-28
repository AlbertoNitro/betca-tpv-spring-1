package es.upm.miw.business_services;

import es.upm.miw.documents.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PdfService {

    private static final String[] TABLE_COLUMNS_HEADERS = {" ", "Desc.", "Ud.", "Dto.%", "€", "E."};
    private static final float[] TABLE_COLUMNS_SIZES_TICKETS = {15, 90, 15, 25, 35, 15};

    private static final String[] TABLE_GIFT_TICKET_COLUMNS_HEADERS = {" ", "Desc.", "Ud.", "E."};
    private static final float[] TABLE_GIFT_TICKET_COLUMNS_SIZES_TICKETS = {15, 150, 15, 15};

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

    @Value("${miw.rgpd.clause1}")
    private String clause1;

    @Value("${miw.rgpd.clause2}")
    private String clause2;

    @Value("${miw.rgpd.clause3}")
    private String clause3;

    @Value("${miw.rgpd.dpd}")
    private String dpd;

    @Value("${miw.rgpd.basic}")
    private String basic;

    @Value("${miw.rgpd.medium}")
    private String medium;

    @Value("${miw.rgpd.advance}")
    private String advance;

    @Value("${miw.rgpd.cancel}")
    private String cancel;

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
        if (isShoppingNotCommitted(shopping)) {
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
        PdfBuilder pdf = new PdfBuilder(path, PdfBuilder.PAGE_SIZE_A4);
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
        PdfBuilder pdf = new PdfBuilder(path);
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

    public byte[] generateGiftTicket(Ticket ticket) {

        final String path = "/tpv-pdfs/tickets/ticket-" + ticket.getId();
        PdfBuilder pdf = new PdfBuilder(path);
        this.generateCommonHead(pdf);
        pdf.paragraphEmphasized("GIFT TICKET");

        pdf.barCode(ticket.getGiftTicket().getId());
        pdf.line();
        pdf.paragraphEmphasized("Fecha de emisión: " + ticket.getGiftTicket().getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        pdf.paragraphEmphasized("Fecha de expiración: " + ticket.getGiftTicket().getExpirationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        int notCommitted = 0;

        PdfTableBuilder table = pdf.table(TABLE_GIFT_TICKET_COLUMNS_SIZES_TICKETS).tableColumnsHeader(TABLE_GIFT_TICKET_COLUMNS_HEADERS);
        for (int i = 0; i < ticket.getShoppingList().length; i++) {
            Shopping shopping = ticket.getShoppingList()[i];
            String state = "";
            if (isShoppingNotCommitted(shopping)) {
                state = "N";
                notCommitted++;
            }
            table.tableCell(String.valueOf(i + 1), shopping.getDescription(), "" + shopping.getAmount(), state);
        }

        table.build();

        if (ticket.getGiftTicket().getNote()!= null && !ticket.getGiftTicket().getNote().isEmpty() && !"null".equals(ticket.getGiftTicket().getNote())){
            pdf.paragraph("" + ticket.getGiftTicket().getNote());
        }

        if (notCommitted > 0) {
            pdf.paragraphEmphasized("Artículos pendientes de entrega: " + notCommitted);
            if (ticket.getUser() != null) {
                addContactData(pdf, ticket.getUser());
            }
            pdf.qrCode(ticket.getReference());
        }

        pdf.line().paragraph("Periodo de devolución o cambio: 15 dias a partir de la fecha del ticket");
        this.generateCommonFooter(pdf);
        return pdf.build();
    }

    private boolean isShoppingNotCommitted(Shopping shopping){
        return shopping.getShoppingState() != ShoppingState.COMMITTED && shopping.getAmount() > 0;
    }

    public void addContactData (PdfBuilder pdf, User user){
        pdf.paragraph("Teléfono de contacto: " + user.getMobile() + " - " + user.getUsername().substring(0,
                (user.getUsername().length() > 10) ? 10 : user.getUsername().length()));
    }

    public byte[] generatePrintableRgpdAgreement(User user, RgpdAgreementType type) {
        final String path = "/rgpd-pdfs/printable/agreement-" + user.getId();
        PdfBuilder pdf = new PdfBuilder(path, PdfBuilder.PAGE_SIZE_A4);
        this.generateCommonHead(pdf);
        pdf.paragraphEmphasized("Contrato de proteccion de datos.");
        pdf.line();
        pdf.paragraphEmphasized("Otorgante: " + user.getUsername());
        pdf.paragraphEmphasized("DNI Otorgante: " + user.getDni());
        pdf.paragraphEmphasized("Fecha contrato: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        pdf.line();
        pdf.paragraph(this.clause1);
        pdf.paragraph(this.clause2);
        pdf.paragraph(this.clause3);
        pdf.line();
        pdf.paragraph(this.dpd);
        pdf.line();
        pdf.paragraphEmphasized(this.basic);
        if (type.equals(RgpdAgreementType.MEDIUM) || type.equals(RgpdAgreementType.ADVANCE)) {
            pdf.line();
            pdf.paragraphEmphasized(this.medium);
        }
        if (type.equals(RgpdAgreementType.ADVANCE)) {
            pdf.line();
            pdf.paragraphEmphasized(this.advance);
        }
        pdf.line();
        pdf.paragraph(this.cancel);
        return pdf.build();
    }
    public byte[] generateInvoice(Invoice invoice, Ticket ticket){
        final String path = "/invoice-" + invoice.getId();
        PdfBuilder pdf = new PdfBuilder(path, PdfBuilder.PAGE_SIZE_A4);
        this.generateCommonHead(pdf);
        BigDecimal total = new BigDecimal(0);
        pdf.paragraphEmphasized("INVOICE");
        pdf.line();
        Shopping[] shoppingList = ticket.getShoppingList();
        pdf.paragraphEmphasized(invoice.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        PdfTableBuilder table = pdf.table(TABLE_COLUMNS_SIZES_TICKETS).tableColumnsHeader(TABLE_COLUMNS_HEADERS);
        for (int i = 0; i < shoppingList.length; i++) {
            Shopping shopping = shoppingList[i];
            System.out.println("Retail Price " + i + " --- " + shopping.getRetailPrice().toString());
            total = total.add(shopping.getRetailPrice().multiply(new BigDecimal(shopping.getAmount())));
            this.generateTableCellFromShopping(table, shopping, i, 0);
        }
        table.build();
        pdf.paragraphEmphasized("Total: " + total.toString());
        pdf.paragraph("ID Invoice: " + invoice.getId());
        pdf.line();
        this.generateCommonFooter(pdf);
        return pdf.build();
    }
}
