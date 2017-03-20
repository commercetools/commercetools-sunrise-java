package com.commercetools.sunrise.search.sort;

import com.commercetools.sunrise.framework.viewmodels.forms.FormOption;

import java.util.List;
import java.util.Locale;

/**
 * An option to sort the results from a query or search on some resource endpoint.
 */
public interface SortFormOption extends FormOption<List<String>> {

    ConfiguredSortFormOption configuration();

    /**
     * Gets the expressions associated with this option, representing the attribute path and sorting direction.
     * These expressions are ready to be sent to the CTP platform.
     * @return the sort expression for this option
     */
    @Override
    List<String> getValue();

    static SortFormOption of(final ConfiguredSortFormOption configuration, final Locale locale) {
        return new SortFormOptionImpl(configuration, locale);
    }
}
