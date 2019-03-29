package es.upm.miw.dtos.input;

import es.upm.miw.dtos.validations.BigDecimalPositive;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class VoucherInputDto {
    @NotNull
    @BigDecimalPositive
    private BigDecimal value;

    public VoucherInputDto(BigDecimal value) {

        this.value = value;
    }

    public VoucherInputDto() {
        // empty  framework
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "VoucherInputDto{" +
                ", value=" + value +
                '}';
    }
}
