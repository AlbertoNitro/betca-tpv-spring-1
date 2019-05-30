package es.upm.miw.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Document
public class Invoice {

    private static final String DATE_FORMAT = "yyyy";

    @Id
    private String id;

    private LocalDateTime creationDate;

    private BigDecimal baseTax;

    private BigDecimal tax;

    @DBRef
    private Ticket ticket;

    @DBRef
    private User user;

    private String referencesPositiveInvoice;

    public Invoice() {
        creationDate = LocalDateTime.now();
    }
    public Invoice(BigDecimal baseTax, BigDecimal tax, String referencesPositiveInvoice) {
      //  this.id = new SimpleDateFormat(DATE_FORMAT).format(new Date()) + idOfYear;
        this.baseTax = baseTax;
        this.tax = tax;
        this.referencesPositiveInvoice = referencesPositiveInvoice;
    }

    public String getReferencespositiveinvoice() {
        return referencesPositiveInvoice;
    }

    public void setReferencespositiveinvoice(String negativeinvoice) {
        this.referencesPositiveInvoice = negativeinvoice;
    }

    public void setBaseTax(BigDecimal baseTax) {
        this.baseTax = baseTax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getId() {
        return id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public int simpleId() {
        return Integer.parseInt(String.valueOf(id).substring(DATE_FORMAT.length()));
    }

    public BigDecimal getBaseTax() {
        return baseTax;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && (id.equals(((Invoice) obj).id));
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id='" + id + '\'' +
                ", creationDate=" + creationDate +
                ", baseTax=" + baseTax +
                ", tax=" + tax +
                ", ticket=" + ticket +
                ", user=" + user +
                '}';
    }
}
