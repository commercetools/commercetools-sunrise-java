package com.commercetools.sunrise.search.facetedsearch.terms;

import com.commercetools.sunrise.framework.viewmodels.forms.WithFormFieldName;
import com.commercetools.sunrise.search.facetedsearch.ConfiguredFacetedSearchFormSettingsWithOptions;

import javax.annotation.Nullable;

public interface ConfiguredTermFacetedSearchFormSettings extends ConfiguredFacetedSearchFormSettingsWithOptions, WithFormFieldName {

    /**
     * Gets the threshold indicating the minimum amount of options allowed to be displayed in the facet.
     * @return the threshold for the amount of options that can be displayed, or absent if it has no threshold
     */
    @Nullable
    Long getThreshold();

    /**
     * Gets the limit for the maximum amount of options allowed to be displayed in the facet.
     * @return the limit for the amount of options that can be displayed, or absent if it has no limit
     */
    @Nullable
    Long getLimit();

    /**
     * Gets the mapper type for this facet.
     * @return the facet option mapper, or absent if there is no mapper
     */
    @Nullable
    TermFacetMapperSettings getMapperSettings();

    static ConfiguredTermFacetedSearchFormSettings of(final String fieldName, final String label, final String expression,
                                                      final boolean isCountDisplayed, @Nullable final String uiType,
                                                      final boolean isMultiSelect, final boolean isMatchingAll,
                                                      @Nullable final TermFacetMapperSettings mapper,
                                                      @Nullable final Long limit, @Nullable final Long threshold) {
        return new ConfiguredTermFacetedSearchFormSettingsImpl(fieldName, label, expression, isCountDisplayed, uiType, isMultiSelect, isMatchingAll, mapper, limit, threshold);
    }
}
