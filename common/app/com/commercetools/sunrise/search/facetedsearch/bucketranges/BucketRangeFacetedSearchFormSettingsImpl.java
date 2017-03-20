package com.commercetools.sunrise.search.facetedsearch.bucketranges;

import com.commercetools.sunrise.search.facetedsearch.AbstractFacetedSearchFormSettingsWithOptions;

import java.util.List;
import java.util.Locale;

final class BucketRangeFacetedSearchFormSettingsImpl<T> extends AbstractFacetedSearchFormSettingsWithOptions<ConfiguredBucketRangeFacetedSearchFormSettings> implements BucketRangeFacetedSearchFormSettings<T> {

    BucketRangeFacetedSearchFormSettingsImpl(final ConfiguredBucketRangeFacetedSearchFormSettings settings, final Locale locale) {
        super(settings, locale);
    }

    @Override
    public String getFieldName() {
        return configuration().getFieldName();
    }

    @Override
    public List<BucketRangeFacetedSearchFormOption> getOptions() {
        return configuration().getOptions();
    }
}
