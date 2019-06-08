package es.upm.miw.dtos.output;

import java.math.BigDecimal;

public class InvoiceDto {

    String id;
    String creationDate;
    BigDecimal baseTax;
    BigDecimal tax;

    public InvoiceDto(String id, String creationDate, BigDecimal baseTax,
                            BigDecimal tax) {
        this.id = id;
        this.creationDate = creationDate;
        this.baseTax = baseTax;
        this.tax = tax;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getBaseTax() {
        return baseTax;
    }

    public void setBaseTax(BigDecimal baseTax) {
        this.baseTax = baseTax;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

}
