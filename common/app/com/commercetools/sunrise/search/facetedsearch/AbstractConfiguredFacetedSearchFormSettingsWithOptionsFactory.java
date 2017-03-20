package com.commercetools.sunrise.search.facetedsearch;

import play.Configuration;

public abstract class AbstractConfiguredFacetedSearchFormSettingsWithOptionsFactory extends AbstractConfiguredFacetedSearchFormSettingsFactory {

    private static final String CONFIG_MULTI_SELECT = "multiSelect";
    private static final String CONFIG_MATCHING_ALL = "matchingAll";

    protected final boolean isMultiSelect(final Configuration configuration) {
        return configuration.getBoolean(CONFIG_MULTI_SELECT, true);
    }

    protected final boolean isMatchingAll(final Configuration configuration) {
        return configuration.getBoolean(CONFIG_MATCHING_ALL, false);
    }
}
