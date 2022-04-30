package exeptions;

public class LoginException extends Exception {
    public LoginException(String message) {
        super(message);
    }

    public LoginException(ErrorMsg msg) {
        super(msg.getMsg());
    }
}
