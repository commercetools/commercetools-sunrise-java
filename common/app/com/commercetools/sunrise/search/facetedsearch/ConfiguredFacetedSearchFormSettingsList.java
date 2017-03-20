package com.commercetools.sunrise.search.facetedsearch;

import java.util.List;

public interface ConfiguredFacetedSearchFormSettingsList {

    List<? extends ConfiguredFacetedSearchFormSettings> getConfiguredSettings();

    static ConfiguredFacetedSearchFormSettingsList of(final List<? extends ConfiguredFacetedSearchFormSettings> configurations) {
        return new ConfiguredFacetedSearchFormSettingsListImpl(configurations);
    }
}
