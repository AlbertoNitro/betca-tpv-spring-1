package es.upm.miw.exceptions;

public class ErrorMessage {

    private String error;

    private String message;

    private String path;

    public ErrorMessage(Exception exception, String path) {
        this.error = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
        this.path = path;
    }

    public ErrorMessage(String error) {
        this.error = error;
        this.message = error;
        this.path = "/";
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "ErrorMessage [error=" + error + ", message=" + message + ", path=" + path + "]";
    }

}
