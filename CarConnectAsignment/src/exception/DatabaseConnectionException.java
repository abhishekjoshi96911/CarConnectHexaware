package exception;

import java.io.Serializable;

public class DatabaseConnectionException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
