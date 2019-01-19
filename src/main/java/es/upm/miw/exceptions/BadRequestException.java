package es.upm.miw.exceptions;

public class BadRequestException extends Exception {
    public static final String DESCRIPTION = "Bad Request Exception";
    private static final long serialVersionUID = 6830756676887746370L;

    public BadRequestException() {
        super(DESCRIPTION);
    }

    public BadRequestException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
