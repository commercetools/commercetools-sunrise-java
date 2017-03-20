package com.commercetools.sunrise.search.facetedsearch.terms;

import com.commercetools.sunrise.search.facetedsearch.AbstractConfiguredFacetedSearchFormSettingsWithOptions;

import javax.annotation.Nullable;

final class ConfiguredTermFacetedSearchFormSettingsImpl extends AbstractConfiguredFacetedSearchFormSettingsWithOptions implements ConfiguredTermFacetedSearchFormSettings {

    private final String fieldName;
    @Nullable
    private final TermFacetMapperSettings mapperSettings;
    @Nullable
    private final Long limit;
    @Nullable
    private final Long threshold;

    ConfiguredTermFacetedSearchFormSettingsImpl(final String fieldName, final String fieldLabel, final String attributePath,
                                                final boolean isCountDisplayed, @Nullable final String uiType,
                                                final boolean multiSelect, final boolean matchingAll,
                                                @Nullable final TermFacetMapperSettings mapperSettings,
                                                @Nullable final Long limit, @Nullable final Long threshold) {
        super(fieldLabel, attributePath, isCountDisplayed, uiType, multiSelect, matchingAll);
        this.fieldName = fieldName;
        this.mapperSettings = mapperSettings;
        this.limit = limit;
        this.threshold = threshold;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    @Nullable
    public TermFacetMapperSettings getMapperSettings() {
        return mapperSettings;
    }

    @Override
    @Nullable
    public Long getLimit() {
        return limit;
    }


    @Override
    @Nullable
    public Long getThreshold() {
        return threshold;
    }
}
