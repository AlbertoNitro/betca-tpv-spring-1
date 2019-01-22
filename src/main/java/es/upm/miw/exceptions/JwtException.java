package es.upm.miw.exceptions;

public class JwtException extends Exception {
    private static final String DESCRIPTION = "Jwt exception";
    private static final long serialVersionUID = -1344640670884805385L;

    public JwtException() {
        this("");
    }

    public JwtException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
