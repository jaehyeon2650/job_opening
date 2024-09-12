package project.general_project.exception;

public class NoTeamException extends RuntimeException {
    public NoTeamException() {
        super();
    }

    public NoTeamException(String message) {
        super(message);
    }

    public NoTeamException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoTeamException(Throwable cause) {
        super(cause);
    }

    protected NoTeamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
