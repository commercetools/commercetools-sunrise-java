package com.commercetools.sunrise.productcatalog.productoverview.search.searchbox;

import com.commercetools.sunrise.framework.SunriseConfigurationException;
import com.commercetools.sunrise.search.searchbox.SearchBoxSettings;
import com.commercetools.sunrise.search.searchbox.SearchBoxSettingsFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public final class ProductSearchBoxSettings implements SearchBoxSettings {

    private static final String CONFIG = "pop.searchTerm";

    private final SearchBoxSettings settings;

    @Inject
    public ProductSearchBoxSettings(final Configuration configuration, final SearchBoxSettingsFactory searchBoxSettingsFactory) {
        this.settings = searchBoxSettingsFactory.create(configuration(configuration));
    }

    @Override
    public String getFieldName() {
        return settings.getFieldName();
    }

    private static Configuration configuration(final Configuration configuration) {
        return Optional.ofNullable(configuration.getConfig(CONFIG))
                .orElseThrow(() -> new SunriseConfigurationException("Could not find search box configuration", CONFIG));
    }
}