package es.upm.miw.exceptions;

public class ConflictRequestException extends RuntimeException {
    private static final String DESCRIPTION = "Conflict Request Exception";

    public ConflictRequestException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
