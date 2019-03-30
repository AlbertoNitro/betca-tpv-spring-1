package es.upm.miw.dtos;

public class RgpdDto {

    private String agreementType;

    private String printableAgreement;

    private boolean accepted;

    public RgpdDto() {
        // Empty for framework
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
