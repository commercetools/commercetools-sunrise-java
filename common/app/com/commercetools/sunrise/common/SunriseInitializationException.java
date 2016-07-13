package com.commercetools.sunrise.common;

public class SunriseInitializationException extends RuntimeException {

    public SunriseInitializationException(final String message) {
        super(message);
    }

    public SunriseInitializationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
