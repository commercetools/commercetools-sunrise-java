package com.commercetools.sunrise.framework.viewmodels.forms;

import play.mvc.Http;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public interface FormSettingsWithOptions<T extends FormOption<V>, V> extends WithFormFieldName, WithFormOptions<T, V> {

    /**
     * Finds all selected valid options for this form in the HTTP request.
     * @param httpContext current HTTP context
     * @return a list of valid selected options for this form
     */
    default List<T> getAllSelectedOptions(final Http.Context httpContext) {
        final List<String> selectedValues = getSelectedValuesAsRawList(httpContext);
        return getOptions().stream()
                .filter(option -> selectedValues.contains(option.getFieldValue()))
                .collect(toList());
    }

    /**
     * Finds one selected valid option for this form in the HTTP request.
     * If many valid values are selected, which one is going to be returned is non-deterministic.
     * @param httpContext current HTTP context
     * @return a valid selected option for this form, or empty if no valid option is selected
     */
    default Optional<T> getSelectedOption(final Http.Context httpContext) {
        return getAllSelectedOptions(httpContext).stream()
                .findAny()
                .map(Optional::of)
                .orElseGet(this::findDefaultOption);
    }
}
