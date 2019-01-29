package es.upm.miw.exceptions;

public class ConflictRequestException extends Exception {
    private static final String DESCRIPTION = "Conflict Request Exception";
    private static final long serialVersionUID = 1564291763389349849L;

    public ConflictRequestException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
