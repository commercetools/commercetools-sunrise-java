package com.commercetools.sunrise.search.facetedsearch.sliderranges;

import com.commercetools.sunrise.search.facetedsearch.AbstractFacetedSearchFormSettings;

import java.util.Locale;

final class SliderRangeFacetedSearchFormSettingsImpl<T> extends AbstractFacetedSearchFormSettings<ConfiguredSliderRangeFacetedSearchFormSettings> implements SliderRangeFacetedSearchFormSettings<T> {

    SliderRangeFacetedSearchFormSettingsImpl(final ConfiguredSliderRangeFacetedSearchFormSettings configuration, final Locale locale) {
        super(configuration, locale);
    }

    @Override
    public RangeEndpointFormSettings getLowerEndpointSettings() {
        return configuration().getLowerEndpointSettings();
    }

    @Override
    public RangeEndpointFormSettings getUpperEndpointSettings() {
        return configuration().getUpperEndpointSettings();
    }
}
