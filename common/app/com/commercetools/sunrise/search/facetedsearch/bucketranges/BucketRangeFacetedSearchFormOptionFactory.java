package com.commercetools.sunrise.search.facetedsearch.bucketranges;

import com.commercetools.sunrise.framework.SunriseConfigurationException;
import com.commercetools.sunrise.framework.SunriseModel;
import play.Configuration;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class BucketRangeFacetedSearchFormOptionFactory extends SunriseModel {

    private static final String OPTION_LABEL_ATTR = "label";
    private static final String OPTION_VALUE_ATTR = "value";
    private static final String OPTION_RANGE_ATTR = "range";

    public BucketRangeFacetedSearchFormOption create(final Configuration configuration) {
        final String fieldLabel = fieldLabel(configuration);
        final String fieldValue = fieldValue(configuration);
        final String range = range(configuration);
        return new BucketRangeFacetedSearchFormOptionImpl(fieldLabel, fieldValue, range);
    }

    protected final String fieldLabel(final Configuration configuration) {
        return configuration.getString(OPTION_LABEL_ATTR, "");
    }

    protected final String fieldValue(final Configuration configuration) {
        return Optional.ofNullable(configuration.getString(OPTION_VALUE_ATTR))
                .orElseThrow(() -> new SunriseConfigurationException("Missing bucket facet field value", OPTION_VALUE_ATTR, configuration));
    }

    protected final String range(final Configuration configuration) {
        return configuration.getString(OPTION_RANGE_ATTR, "");
    }
}
