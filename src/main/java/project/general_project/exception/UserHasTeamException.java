package project.general_project.exception;

public class UserHasTeamException extends RuntimeException{
    public UserHasTeamException() {
        super();
    }

    public UserHasTeamException(String message) {
        super(message);
    }

    public UserHasTeamException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserHasTeamException(Throwable cause) {
        super(cause);
    }

    protected UserHasTeamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
