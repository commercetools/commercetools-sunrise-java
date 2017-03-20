package com.commercetools.sunrise.search.facetedsearch.sliderranges;

import com.commercetools.sunrise.framework.SunriseModel;
import com.commercetools.sunrise.framework.injection.RequestScoped;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class SliderRangeFacetedSearchFormSettingsFactory extends SunriseModel {

    private final Locale locale;

    @Inject
    public SliderRangeFacetedSearchFormSettingsFactory(final Locale locale) {
        this.locale = locale;
    }

    public <T> SliderRangeFacetedSearchFormSettings<T> create(final ConfiguredSliderRangeFacetedSearchFormSettings configuration) {
        return new SliderRangeFacetedSearchFormSettingsImpl<>(configuration, locale);
    }
}
