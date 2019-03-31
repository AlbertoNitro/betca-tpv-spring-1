package es.upm.miw.documents;

public enum RgpdAgreementType {
    BASIC("1"),
    MEDIUM("2"),
    ADVANCE("3");

    private final String type;

    RgpdAgreementType(String type) {
        this.type = type;
    }

    public static RgpdAgreementType getRgpdAgreementType(String type) {
        if (type.equals("1"))
            return RgpdAgreementType.BASIC;
        if (type.equals("2"))
            return RgpdAgreementType.MEDIUM;
        if (type.equals("3"))
            return RgpdAgreementType.ADVANCE;
        return null;
    }

    public boolean equals(String otherType) {
        return this.type.equals(otherType);
    }

    public String toString() {
        return this.type;
    }
}
