package es.upm.miw.exceptions;

public class FileException extends Exception {
    private static final long serialVersionUID = -971479862516984984L;

    public static final String DESCRIPTION = "File exception";

    public FileException() {
        super(DESCRIPTION);
    }

    public FileException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
