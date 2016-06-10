package com.commercetools.sunrise.common.inject;

public class SunriseInitializationException extends RuntimeException {

    public SunriseInitializationException(final String message) {
        super(message);
    }

    public SunriseInitializationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
