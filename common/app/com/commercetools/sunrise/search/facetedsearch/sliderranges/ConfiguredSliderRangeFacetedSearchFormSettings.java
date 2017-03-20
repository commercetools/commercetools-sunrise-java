package com.commercetools.sunrise.search.facetedsearch.sliderranges;

import com.commercetools.sunrise.search.facetedsearch.ConfiguredFacetedSearchFormSettings;

import javax.annotation.Nullable;

public interface ConfiguredSliderRangeFacetedSearchFormSettings extends ConfiguredFacetedSearchFormSettings {

    RangeEndpointFormSettings getLowerEndpointSettings();

    RangeEndpointFormSettings getUpperEndpointSettings();

    static ConfiguredSliderRangeFacetedSearchFormSettings of(final String label, final String attributePath,
                                                             final boolean isCountDisplayed, @Nullable final String uiType,
                                                             final RangeEndpointFormSettings lowerEndpointSettings,
                                                             final RangeEndpointFormSettings upperEndpointSettings) {
        return new ConfiguredSliderRangeFacetedSearchFormSettingsImpl(label, attributePath, isCountDisplayed, uiType, lowerEndpointSettings, upperEndpointSettings);
    }
}
