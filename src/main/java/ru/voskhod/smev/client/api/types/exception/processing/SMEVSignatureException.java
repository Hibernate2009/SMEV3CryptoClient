package ru.voskhod.smev.client.api.types.exception.processing;

import ru.voskhod.smev.client.api.types.exception.SMEVProcessingException;


/**
 * The type Smev signature exception.
 */
public class SMEVSignatureException extends SMEVProcessingException {
    /**
     * The Description.
     */
    protected String description;

    /**
     * Instantiates a new Smev signature exception.
     */
    public SMEVSignatureException() {
        super(null);
    }

    /**
     * Instantiates a new Smev signature exception.
     *
     * @param message the message
     * @param code    the code
     * @param cause   the cause
     */
    public SMEVSignatureException(String message, String code, Throwable cause) {
        super(message, cause, code);
    }

    /**
     * Instantiates a new Smev signature exception.
     *
     * @param message the message
     */
    public SMEVSignatureException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Smev signature exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public SMEVSignatureException(String message, Throwable cause) {
        super(message, cause, null);
    }

    /**
     * Instantiates a new Smev signature exception.
     *
     * @param message     the message
     * @param cause       the cause
     * @param description the description
     */
    public SMEVSignatureException(String message, Throwable cause, String description) {
        super(message, cause, null);
        this.description = description;
    }

    /**
     * Instantiates a new Smev signature exception.
     *
     * @param cause the cause
     */
    public SMEVSignatureException(Throwable cause) {
        super(cause, null);
    }

    /**
     * Instantiates a new Smev signature exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    public SMEVSignatureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace, null);
    }

    /**
     * The type Signature verification fault exception.
     */
    public static class SignatureVerificationFaultException
            extends SMEVSignatureException {

        /**
         * Java type that goes as soapenv:Fault detail element.
         */
        private String faultInfo;

        /**
         * Instantiates a new Signature verification fault exception.
         *
         * @param message the message
         * @param ex      the ex
         */
        public SignatureVerificationFaultException(String message, Throwable ex) {
            super(message, ex);
        }

        /**
         * Instantiates a new Signature verification fault exception.
         *
         * @param message   the message
         * @param faultInfo the fault info
         * @param cause     the cause
         */
        public SignatureVerificationFaultException(String message, String faultInfo, Throwable cause) {
            super(message, cause);
            this.faultInfo = faultInfo;
        }

        /**
         * Instantiates a new Signature verification fault exception.
         *
         * @param message   the message
         * @param faultInfo the fault info
         */
        public SignatureVerificationFaultException(String message, String faultInfo) {
            super(message, null);
            this.faultInfo = faultInfo;

        }

        /**
         * Instantiates a new Signature verification fault exception.
         *
         * @param message     the message
         * @param faultInfo   the fault info
         * @param description the description
         * @param cause       the cause
         */
        public SignatureVerificationFaultException(String message, String faultInfo, String description, Throwable cause) {
            super(message, cause, description);
            this.faultInfo = faultInfo;

        }

        /**
         * Gets description.
         *
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets fault info.
         *
         * @return the fault info
         */
        public String getFaultInfo() {
            return faultInfo;
        }

    }
}
