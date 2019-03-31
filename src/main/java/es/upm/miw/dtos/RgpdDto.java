package es.upm.miw.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.documents.RgpdAgreement;
import es.upm.miw.documents.RgpdAgreementType;
import javax.validation.constraints.NotNull;
import java.util.Base64;

public class RgpdDto {

    @NotNull
    private String agreementType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String printableAgreement;

    private boolean accepted;

    public RgpdDto() {
        this.agreementType = RgpdAgreementType.BASIC.toString();
        this.accepted = false;
    }

    public RgpdDto(RgpdAgreement rgpd) {
        this.printableAgreement = Base64.getEncoder().encodeToString(rgpd.getAgreement());
        this.accepted = true;
        this.setAgreementType(rgpd.getType().toString());
    }

    public String getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType;
    }


    public String getPrintableAgreement() {
        return printableAgreement;
    }

    public void setPrintableAgreement(String printableAgreement) {
        this.printableAgreement = printableAgreement;
    }

    public void setPrintableAgreement(byte[] printableAgreement) {
        this.printableAgreement = Base64.getEncoder().encodeToString(printableAgreement);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public String toString() {
        return "RgpdDto{" +
                "agreementType='" + agreementType + '\'' +
                ", printableAgreement='" + printableAgreement + '\'' +
                '}';
    }
}
