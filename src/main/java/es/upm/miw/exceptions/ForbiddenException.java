package es.upm.miw.exceptions;

public class ForbiddenException extends Exception {
    public static final String DESCRIPTION = "Forbidden. Insufficient role";
    private static final long serialVersionUID = -1344640670884805385L;

    public ForbiddenException() {
        super(DESCRIPTION);
    }

    public ForbiddenException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
