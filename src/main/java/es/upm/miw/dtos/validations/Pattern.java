package es.upm.miw.dtos.validations;

public final class Pattern {
    public static final String NINE_DIGITS = "\\d{9}";
    public static final String EMAIL_PATTERN = "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";

    private Pattern() {
        // Nothing to do
    }
}
