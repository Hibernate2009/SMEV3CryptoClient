package ru.voskhod.smev.client.api.types.exception;


/**
 * The type Smev runtime exception.
 */
public class SMEVRuntimeException extends SMEVException {
    /**
     * Instantiates a new Smev runtime exception.
     */
    public SMEVRuntimeException() {
    }

    /**
     * Instantiates a new Smev runtime exception.
     *
     * @param message the message
     */
    public SMEVRuntimeException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Smev runtime exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public SMEVRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Smev runtime exception.
     *
     * @param cause the cause
     */
    public SMEVRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Smev runtime exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    public SMEVRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
