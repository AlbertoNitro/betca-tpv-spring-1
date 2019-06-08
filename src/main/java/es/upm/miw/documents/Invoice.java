package es.upm.miw.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document
public class Invoice {

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
        this.creationDate = LocalDateTime.now();
        this.baseTax = baseTax;
        this.tax = tax;
        this.referencesPositiveInvoice = referencesPositiveInvoice;
    }
    public Invoice(BigDecimal baseTax, BigDecimal tax, Ticket ticket, String referencesPositiveInvoice) {
        this.creationDate = LocalDateTime.now();
        this.baseTax = baseTax;
        this.tax = tax;
        this.ticket = ticket;
        this.referencesPositiveInvoice = referencesPositiveInvoice;
    }
    public Invoice(BigDecimal baseTax, BigDecimal tax, Ticket ticket){
        this.creationDate = LocalDateTime.now();
        this.baseTax = baseTax;
        this.tax = tax;
        this.ticket = ticket;
    }
    public Invoice(BigDecimal baseTax, BigDecimal tax){
        this.creationDate = LocalDateTime.now();
        this.baseTax = baseTax;
        this.tax = tax;
    }


    public String getReferencesPositiveInvoice() {
        return referencesPositiveInvoice;
    }

    public void setReferencesPositiveInvoice(String negativeinvoice) {
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

    public void setId(String id) { this.id =id; }

    public Ticket getTicket() {
        return ticket;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
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
