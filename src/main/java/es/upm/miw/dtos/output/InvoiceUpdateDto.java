package es.upm.miw.dtos.output;

public class InvoiceUpdateDto {
    String id;
    String creationDate;
    float baseTax;
    float tax;

    public InvoiceUpdateDto(String id, String creationDate, float baseTax, float tax) {
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

    public float getBaseTax() {
        return baseTax;
    }

    public void setBaseTax(float baseTax) {
        this.baseTax = baseTax;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }
}
