package by.financialstatements.model.exception;
//custom exception to handle login errors
public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
