package com.commercetools.sunrise.search.facetedsearch.bucketranges;

import com.commercetools.sunrise.framework.SunriseModel;
import com.commercetools.sunrise.framework.injection.RequestScoped;

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
