package com.commercetools.sunrise.search.sort;

import com.commercetools.sunrise.framework.viewmodels.forms.FormOption;

import java.util.List;

/**
 * An option to sort the results from a query or search on some resource endpoint.
 */
public interface ConfiguredSortFormOption extends FormOption<List<String>> {

    /**
     * Gets the sort expressions associated with this option, representing the attribute path and sorting direction.
     * The expressions might contain {@code {{locale}}}, which should be replaced with the current locale before using them.
     * @return the sort expression for this option
     */
    @Override
    List<String> getValue();

    static ConfiguredSortFormOption of(final String fieldLabel, final String fieldValue, final List<String> expressions, final boolean isDefault) {
        return new ConfiguredSortFormOptionImpl(fieldLabel, fieldValue, expressions, isDefault);
    }
}
