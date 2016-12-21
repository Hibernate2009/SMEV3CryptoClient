package ru.voskhod.smev.client.api.types.exception;


/**
 * The type Smev exception.
 */
public class SMEVException extends Exception {
    /**
     * Instantiates a new Smev exception.
     */
    public SMEVException() {
    }

    /**
     * Instantiates a new Smev exception.
     *
     * @param message the message
     */
    public SMEVException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Smev exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public SMEVException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Smev exception.
     *
     * @param cause the cause
     */
    public SMEVException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Smev exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    public SMEVException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Re throw cause.
     *
     * @throws Throwable the throwable
     */
    public void reThrowCause() throws Throwable {
        if (getCause() != null) {
            throw getCause();
        }
    }
}
