package com.commercetools.sunrise.contexts;

public class NoCurrencyFoundException extends RuntimeException {

    public NoCurrencyFoundException(final String message) {
        super(message);
    }

    public NoCurrencyFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NoCurrencyFoundException(final Throwable cause) {
        super(cause);
    }
}
