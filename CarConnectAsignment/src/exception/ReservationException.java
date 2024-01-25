package exception;

import java.io.Serializable;

public class ReservationException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public ReservationException(String message) {
        super(message);
    }
}
