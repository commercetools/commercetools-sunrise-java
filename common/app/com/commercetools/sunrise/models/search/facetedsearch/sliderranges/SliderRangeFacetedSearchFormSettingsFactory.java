package com.commercetools.sunrise.models.search.facetedsearch.sliderranges;

import com.commercetools.sunrise.core.SunriseModel;
import com.commercetools.sunrise.core.injection.RequestScoped;

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
