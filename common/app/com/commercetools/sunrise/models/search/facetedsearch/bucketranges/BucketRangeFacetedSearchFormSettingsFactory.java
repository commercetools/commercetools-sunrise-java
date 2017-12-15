package com.commercetools.sunrise.models.search.facetedsearch.bucketranges;

import com.commercetools.sunrise.core.SunriseModel;
import com.commercetools.sunrise.core.injection.RequestScoped;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class BucketRangeFacetedSearchFormSettingsFactory extends SunriseModel {

    private final Locale locale;

    @Inject
    public BucketRangeFacetedSearchFormSettingsFactory(final Locale locale) {
        this.locale = locale;
    }

    public <T> BucketRangeFacetedSearchFormSettings<T> create(final ConfiguredBucketRangeFacetedSearchFormSettings configuration) {
        return new BucketRangeFacetedSearchFormSettingsImpl<>(configuration, locale);
    }
}
