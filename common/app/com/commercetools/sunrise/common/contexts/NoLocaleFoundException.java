package com.commercetools.sunrise.common.contexts;

public class NoLocaleFoundException extends RuntimeException {

    public NoLocaleFoundException(final String message) {
        super(message);
    }

    public NoLocaleFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NoLocaleFoundException(final Throwable cause) {
        super(cause);
    }
}
