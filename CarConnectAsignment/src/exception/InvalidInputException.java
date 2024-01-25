package exception;

import java.io.Serializable;

public class InvalidInputException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(message);
    }
}
