package com.commercetools.sunrise.common.utils;

import com.google.inject.ImplementedBy;
import play.data.validation.ValidationError;

import java.util.List;
import java.util.Locale;

/**
 * Allows to format errors according to the available locales and error information.
 */
@ImplementedBy(ErrorFormatterImpl.class)
public interface ErrorFormatter {

    /**
     * Formats the error message somehow, with the translation to the first available locale.
     * @param locales current given locales
     * @param message error message
     * @return the error message localized and formatted
     */
    String format(final List<Locale> locales, final String message);

    /**
     * Formats the Play error message somehow, with the translation to the first available locale.
     * @param locales current given locales
     * @param error Play's validation error
     * @return the error message localized and formatted
     */
    default String format(final List<Locale> locales, final ValidationError error) {
        final String message = format(locales, error.message());
        return !error.key().isEmpty() ? message + ": " + error.key() : message;
    }
}
