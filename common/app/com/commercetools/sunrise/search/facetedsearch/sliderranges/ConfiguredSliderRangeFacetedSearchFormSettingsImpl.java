package com.commercetools.sunrise.search.facetedsearch.sliderranges;

import com.commercetools.sunrise.search.facetedsearch.AbstractConfiguredFacetedSearchFormSettings;

import javax.annotation.Nullable;

final class ConfiguredSliderRangeFacetedSearchFormSettingsImpl extends AbstractConfiguredFacetedSearchFormSettings implements ConfiguredSliderRangeFacetedSearchFormSettings {

    private final RangeEndpointFormSettings lowerEndpointSettings;
    private final RangeEndpointFormSettings upperEndpointSettings;

    ConfiguredSliderRangeFacetedSearchFormSettingsImpl(final String fieldLabel, final String attributePath, final boolean countDisplayed,
                                                       @Nullable final String uiType, final RangeEndpointFormSettings lowerEndpointSettings,
                                                       final RangeEndpointFormSettings upperEndpointSettings) {
        super(fieldLabel, attributePath, countDisplayed, uiType);
        this.lowerEndpointSettings = lowerEndpointSettings;
        this.upperEndpointSettings = upperEndpointSettings;
    }

    @Override
    public RangeEndpointFormSettings getLowerEndpointSettings() {
        return lowerEndpointSettings;
    }

    @Override
    public RangeEndpointFormSettings getUpperEndpointSettings() {
        return upperEndpointSettings;
    }
}
