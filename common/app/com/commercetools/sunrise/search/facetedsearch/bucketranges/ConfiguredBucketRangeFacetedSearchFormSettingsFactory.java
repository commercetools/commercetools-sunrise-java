package com.commercetools.sunrise.search.facetedsearch.bucketranges;

import com.commercetools.sunrise.search.facetedsearch.AbstractConfiguredFacetedSearchFormSettingsWithOptionsFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Singleton
public class ConfiguredBucketRangeFacetedSearchFormSettingsFactory extends AbstractConfiguredFacetedSearchFormSettingsWithOptionsFactory {

    private static final String CONFIG_RANGES = "ranges";

    private final BucketRangeFacetedSearchFormOptionFactory bucketRangeFacetedSearchFormOptionFactory;

    @Inject
    public ConfiguredBucketRangeFacetedSearchFormSettingsFactory(final BucketRangeFacetedSearchFormOptionFactory bucketRangeFacetedSearchFormOptionFactory) {
        this.bucketRangeFacetedSearchFormOptionFactory = bucketRangeFacetedSearchFormOptionFactory;
    }

    public ConfiguredBucketRangeFacetedSearchFormSettings create(final Configuration configuration) {
        final String fieldName = fieldName(configuration);
        final String fieldLabel = fieldLabel(configuration);
        final String attributePath = attributePath(configuration);
        final boolean countDisplayed = isCountDisplayed(configuration);
        final String uiType = uiType(configuration);
        final boolean multiSelect = isMultiSelect(configuration);
        final boolean matchingAll = isMatchingAll(configuration);
        final List<BucketRangeFacetedSearchFormOption> options = options(configuration);
        return new ConfiguredBucketRangeFacetedSearchFormSettingsImpl(fieldName, fieldLabel, attributePath, countDisplayed, uiType, multiSelect, matchingAll, options);
    }

    protected final List<BucketRangeFacetedSearchFormOption> options(final Configuration configuration) {
        return configuration.getConfigList(CONFIG_RANGES, emptyList()).stream()
                .map(bucketRangeFacetedSearchFormOptionFactory::create)
                .collect(toList());
    }
}
