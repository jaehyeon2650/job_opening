package project.general_project.exception;

public class NoUserException extends RuntimeException {
    public NoUserException() {
        super();
    }

    public NoUserException(String message) {
        super(message);
    }

    public NoUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoUserException(Throwable cause) {
        super(cause);
    }

    protected NoUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
