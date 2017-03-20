package com.commercetools.sunrise.search.facetedsearch.bucketranges;

import com.commercetools.sunrise.search.facetedsearch.AbstractConfiguredFacetedSearchFormSettingsWithOptions;

import javax.annotation.Nullable;
import java.util.List;

class ConfiguredBucketRangeFacetedSearchFormSettingsImpl extends AbstractConfiguredFacetedSearchFormSettingsWithOptions implements ConfiguredBucketRangeFacetedSearchFormSettings {

    private final String fieldName;
    private final List<BucketRangeFacetedSearchFormOption> options;

    ConfiguredBucketRangeFacetedSearchFormSettingsImpl(final String fieldName, final String fieldLabel, final String attributePath,
                                                       final boolean countDisplayed, @Nullable final String uiType,
                                                       final boolean multiSelect, final boolean matchingAll,
                                                       final List<BucketRangeFacetedSearchFormOption> options) {
        super(fieldLabel, attributePath, countDisplayed, uiType, multiSelect, matchingAll);
        this.fieldName = fieldName;
        this.options = options;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public List<BucketRangeFacetedSearchFormOption> getOptions() {
        return options;
    }
}
