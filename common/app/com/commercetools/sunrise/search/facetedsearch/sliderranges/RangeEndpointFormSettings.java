package com.commercetools.sunrise.search.facetedsearch.sliderranges;

import com.commercetools.sunrise.framework.viewmodels.forms.FormSettingsWithDefault;

import javax.annotation.Nullable;
import java.util.Optional;

public interface RangeEndpointFormSettings extends FormSettingsWithDefault<String> {

    @Override
    default String getDefaultValue() {
        return "*";
    }

    @Override
    default Optional<String> mapFieldValueToValue(final String fieldValue) {
        return Optional.of(fieldValue);
    }

    @Override
    default boolean isValidValue(@Nullable final String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Generates the facet options according to the facet result provided.
     * @param fieldName the name of the field for this endpoint
     * @return the generated facet options
     */
    static RangeEndpointFormSettings of(final String fieldName) {
        return new RangeEndpointFormSettingsImpl(fieldName);
    }
}
