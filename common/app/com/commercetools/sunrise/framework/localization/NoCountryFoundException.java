package com.commercetools.sunrise.framework.localization;

public class NoCountryFoundException extends RuntimeException {

    public NoCountryFoundException(final String message) {
        super(message);
    }

    public NoCountryFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
