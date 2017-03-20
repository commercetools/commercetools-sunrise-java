package com.commercetools.sunrise.search.facetedsearch;

import com.commercetools.sunrise.framework.SunriseModel;

import java.util.List;

final class ConfiguredFacetedSearchFormSettingsListImpl extends SunriseModel implements ConfiguredFacetedSearchFormSettingsList {

    private final List<? extends ConfiguredFacetedSearchFormSettings> settings;

    ConfiguredFacetedSearchFormSettingsListImpl(final List<? extends ConfiguredFacetedSearchFormSettings> settings) {
        this.settings = settings;
    }

    @Override
    public List<? extends ConfiguredFacetedSearchFormSettings> getConfiguredSettings() {
        return settings;
    }
}
