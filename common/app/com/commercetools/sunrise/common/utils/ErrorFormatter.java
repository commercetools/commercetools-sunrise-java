package com.commercetools.sunrise.common.utils;

import com.google.inject.ImplementedBy;
import play.data.validation.ValidationError;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Allows to format errors according to the available locales and error information.
 */
@ImplementedBy(ErrorFormatterImpl.class)
public interface ErrorFormatter {

    /**
     * Formats the error message somehow, with the translation to the first available locale.
     * @param locales current given locales
     * @param message error message
     * @param args list of named arguments
     * @return the error message localized and formatted
     */
    String format(final List<Locale> locales, final String message, final Map<String, Object> args);

    /**
     * Formats the Play error message somehow, with the translation to the first available locale.
     * As hash arguments, it defines the field key as "field" and all other arguments as its index, e.g. "0", "1", "2".
     * @param locales current given locales
     * @param error Play's validation error
     * @return the error message localized and formatted
     */
    default String format(final List<Locale> locales, final ValidationError error) {
        final Map<String, Object> args = new HashMap<>();
        if (error.key() != null && !error.key().isEmpty()) {
            args.put("field", error.key());
        }
        IntStream.range(0, error.arguments().size())
                .forEach(index -> args.put(String.valueOf(index), error.arguments().get(index)));
        return format(locales, error.message(), args);
    }
}
