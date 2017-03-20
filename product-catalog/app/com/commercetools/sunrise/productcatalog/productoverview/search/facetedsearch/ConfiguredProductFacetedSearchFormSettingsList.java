package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch;

import com.commercetools.sunrise.search.facetedsearch.ConfiguredFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.ConfiguredFacetedSearchFormSettingsList;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Singleton
public final class ConfiguredProductFacetedSearchFormSettingsList implements ConfiguredFacetedSearchFormSettingsList {

    private static final String CONFIG = "pop.facets";

    private final ConfiguredFacetedSearchFormSettingsList settingsList;

    @Inject
    public ConfiguredProductFacetedSearchFormSettingsList(final Configuration configuration,
                                                          final ConfiguredProductFacetedSearchFormSettingsListFactory configuredProductFacetedSearchFormSettingsListFactory) {
        this.settingsList = configuredProductFacetedSearchFormSettingsListFactory.create(configurationList(configuration));
    }

    @Override
    public List<? extends ConfiguredFacetedSearchFormSettings> getConfiguredSettings() {
        return settingsList.getConfiguredSettings();
    }

    private static List<Configuration> configurationList(final Configuration configuration) {
        return Optional.ofNullable(configuration.getConfigList(CONFIG)).orElseGet(Collections::emptyList);
    }
}
