package es.upm.miw.exceptions;

public class SeederException extends Exception {
    private static final String DESCRIPTION = "Seeder exception";
    private static final long serialVersionUID = -971479862516984984L;

    public SeederException() {
        super(DESCRIPTION);
    }

    public SeederException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
