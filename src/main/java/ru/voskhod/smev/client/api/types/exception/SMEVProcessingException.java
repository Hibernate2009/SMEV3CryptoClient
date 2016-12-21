package ru.voskhod.smev.client.api.types.exception;

/**
 * The type Smev processing exception.
 */
public abstract class SMEVProcessingException extends SMEVException {

    private String code;

    /**
     * Instantiates a new Smev processing exception.
     *
     * @param code the code
     */
    public SMEVProcessingException(String code) {
        this.code = code;
    }

    /**
     * Instantiates a new Smev processing exception.
     *
     * @param message the message
     * @param code    the code
     */
    public SMEVProcessingException(String message, String code) {
        super(message);
        this.code = code;
    }

    /**
     * Instantiates a new Smev processing exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param code    the code
     */
    public SMEVProcessingException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    /**
     * Instantiates a new Smev processing exception.
     *
     * @param cause the cause
     * @param code  the code
     */
    public SMEVProcessingException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }

    /**
     * Instantiates a new Smev processing exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     * @param code               the code
     */
    public SMEVProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }
}
