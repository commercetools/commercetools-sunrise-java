package com.commercetools.sunrise.search.facetedsearch.sliderranges;

import com.commercetools.sunrise.framework.viewmodels.forms.FormSettings;

import javax.annotation.Nullable;

public interface RangeEndpointFormSettings extends FormSettings<String> {

    @Override
    default String getDefaultValue() {
        return "*";
    }

    @Nullable
    @Override
    default String mapFieldValueToValue(final String fieldValue) {
        return fieldValue;
    }

    @Override
    default boolean isValidValue(@Nullable final String value) {
        return value != null;
    }

    /**
     * Generates the facet options according to the facet result provided.
     * @return the generated facet options
     */
    static RangeEndpointFormSettings of(final String fieldName) {
        return new RangeEndpointFormSettingsImpl(fieldName);
    }
}
