package es.upm.miw.exceptions;

public class ForbiddenException extends Exception {
    private static final String DESCRIPTION = "Forbidden. Insufficient role";
    private static final long serialVersionUID = -1344640670884805385L;

    public ForbiddenException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
