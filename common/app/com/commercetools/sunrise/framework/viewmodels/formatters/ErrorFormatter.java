package com.commercetools.sunrise.framework.viewmodels.formatters;

import com.google.inject.ImplementedBy;
import play.data.validation.ValidationError;

/**
 * Allows to format errors according to the available locales and error information.
 */
@ImplementedBy(ErrorFormatterImpl.class)
public interface ErrorFormatter {

    /**
     * Formats the error message somehow.
     * @param message error message
     * @return the error message localized and formatted
     */
    String format(final String message);

    /**
     * Formats the Play error message somehow.
     * @param error Play's validation error
     * @return the error message localized and formatted
     */
    default String format(final ValidationError error) {
        final String message = format(error.message());
        return !error.key().isEmpty() ? message + ": " + error.key() : message;
    }
}
