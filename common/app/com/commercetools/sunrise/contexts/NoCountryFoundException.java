package com.commercetools.sunrise.contexts;

public class NoCountryFoundException extends RuntimeException {

    public NoCountryFoundException(final String message) {
        super(message);
    }

    public NoCountryFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NoCountryFoundException(final Throwable cause) {
        super(cause);
    }
}
