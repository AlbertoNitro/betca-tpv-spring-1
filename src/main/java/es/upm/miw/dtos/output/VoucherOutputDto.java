package es.upm.miw.dtos.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.documents.Voucher;
import es.upm.miw.dtos.validations.BigDecimalPositive;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VoucherOutputDto {
    @NotNull
    private String id;
    @BigDecimalPositive
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal value;
    @NotNull
    private LocalDateTime creationDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime dateOfUse;

    public VoucherOutputDto() {
        // empty  framework
    }
    public VoucherOutputDto(Voucher voucher){
        this.id=voucher.getId();
        this.value=voucher.getValue();
        this.creationDate=voucher.getCreationDate();
    }

    public VoucherOutputDto(@NotNull BigDecimal value,@NotNull LocalDateTime creationDate, LocalDateTime dateOfUse) {

        this.value = value;
        this.creationDate = creationDate;
        this.dateOfUse = dateOfUse;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getDateOfUse() {
        return dateOfUse;
    }

    public void setDateOfUse(LocalDateTime dateOfUse) {
        this.dateOfUse = dateOfUse;
    }

    @Override
    public String toString() {
        return "VoucherOutputDto{" +
                "id='" + id + '\'' +
                ", value=" + value +
                ", creationDate=" + creationDate +
                ", dateOfUse=" + dateOfUse +
                '}';
    }
}
