package com.commercetools.sunrise.productcatalog.productoverview.search.sort;

import com.commercetools.sunrise.framework.SunriseConfigurationException;
import com.commercetools.sunrise.search.sort.ConfiguredSortFormOption;
import com.commercetools.sunrise.search.sort.ConfiguredSortFormSettings;
import com.commercetools.sunrise.search.sort.ConfiguredSortFormSettingsFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public final class ConfiguredProductSortFormSettings implements ConfiguredSortFormSettings {

    private static final String CONFIG = "pop.sortProducts";

    private final ConfiguredSortFormSettings settings;

    @Inject
    public ConfiguredProductSortFormSettings(final Configuration configuration,
                                             final ConfiguredSortFormSettingsFactory configuredSortFormSettingsFactory) {
        this.settings = configuredSortFormSettingsFactory.create(configuration(configuration));
    }

    @Override
    public List<ConfiguredSortFormOption> getOptions() {
        return settings.getOptions();
    }

    @Override
    public String getFieldName() {
        return settings.getFieldName();
    }

    private static Configuration configuration(final Configuration configuration) {
        return Optional.ofNullable(configuration.getConfig(CONFIG))
                .orElseThrow(() -> new SunriseConfigurationException("Could not find sort configuration", CONFIG));
    }
}
