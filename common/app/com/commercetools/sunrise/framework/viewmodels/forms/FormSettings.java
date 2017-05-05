package com.commercetools.sunrise.framework.viewmodels.forms;

import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public interface FormSettings<T> extends WithFormFieldName {

    Optional<T> mapFieldValueToValue(final String fieldValue);

    boolean isValidValue(@Nullable final T value);

    /**
     * Finds all selected valid values for this form in the HTTP request.
     * @param httpContext current HTTP context
     * @return a list of valid selected values for this form
     */
    default List<T> getAllSelectedValues(final Http.Context httpContext) {
        return getSelectedValuesAsRawList(httpContext).stream()
                .map(this::mapFieldValueToValue)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(this::isValidValue)
                .collect(toList());
    }

    /**
     * Finds one selected valid value for this form in the HTTP request.
     * If many valid values are selected, which one is going to be returned is non-deterministic.
     * @param httpContext current HTTP context
     * @return a valid selected value for this form, or empty if no valid value is selected
     */
    default Optional<T> getSelectedValue(final Http.Context httpContext) {
        return getAllSelectedValues(httpContext).stream()
                .findAny();
    }
}

