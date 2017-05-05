package com.commercetools.sunrise.framework.viewmodels.forms;

import play.mvc.Http;

public interface FormSettingsWithDefault<T> extends FormSettings<T> {

    T getDefaultValue();

    /**
     * Finds one selected valid value for this form in the HTTP request.
     * If many valid values are selected, which one is going to be returned is non-deterministic.
     * @param httpContext current HTTP context
     * @return a valid selected value for this form, or the default value if no valid value is selected
     */
    default T getSelectedValueOrDefault(final Http.Context httpContext) {
        return getAllSelectedValues(httpContext).stream()
                .findAny()
                .orElseGet(this::getDefaultValue);
    }
}

