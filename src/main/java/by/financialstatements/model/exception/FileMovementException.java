package by.financialstatements.model.exception;
//custom exception for handling errors when moving or working with files
public class FileMovementException extends RuntimeException {
    public FileMovementException(String message, Throwable cause) {
        super(message, cause);
    }
}
