package es.upm.miw.dtos;

import es.upm.miw.documents.RgpdAgreement;
import es.upm.miw.documents.RgpdAgreementType;

import java.util.Base64;

public class RgpdDto {

    private String agreementType;

    private String printableAgreement;

    private boolean accepted;

    public RgpdDto() {
        this.accepted = false;
    }

    public RgpdDto(RgpdAgreement rgpd) {
        this.printableAgreement = Base64.getEncoder().encodeToString(rgpd.getAgreement());
        this.accepted = true;
        if (rgpd.getType().equals(RgpdAgreementType.BASIC))
            this.agreementType = "1";
        else if (rgpd.getType().equals(RgpdAgreementType.MEDIUM))
            this.agreementType = "2";
        else if (rgpd.getType().equals(RgpdAgreementType.ADVANCE))
            this.agreementType = "3";
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
