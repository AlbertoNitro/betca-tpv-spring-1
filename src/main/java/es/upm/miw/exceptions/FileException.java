package es.upm.miw.exceptions;

public class FileException extends Exception {
    private static final String DESCRIPTION = "File exception";
    private static final long serialVersionUID = -971479862516984984L;

    public FileException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
