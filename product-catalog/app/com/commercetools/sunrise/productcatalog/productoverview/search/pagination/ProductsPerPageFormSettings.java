package com.commercetools.sunrise.productcatalog.productoverview.search.pagination;

import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.commercetools.sunrise.search.pagination.EntriesPerPageFormOption;
import com.commercetools.sunrise.search.pagination.EntriesPerPageFormSettings;
import com.commercetools.sunrise.search.pagination.EntriesPerPageFormSettingsFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public final class ProductsPerPageFormSettings implements EntriesPerPageFormSettings {

    private static final String CONFIG = "pop.productsPerPage";

    private final EntriesPerPageFormSettings settings;

    @Inject
    public ProductsPerPageFormSettings(final Configuration configuration, final EntriesPerPageFormSettingsFactory entriesPerPageFormSettingsFactory) {
        this.settings = entriesPerPageFormSettingsFactory.create(configuration(configuration));
    }

    @Override
    public List<EntriesPerPageFormOption> getOptions() {
        return settings.getOptions();
    }

    @Override
    public String getFieldName() {
        return settings.getFieldName();
    }

    private static Configuration configuration(final Configuration configuration) {
        return Optional.ofNullable(configuration.getConfig(CONFIG))
                .orElseThrow(() -> new SunriseConfigurationException("Could not find products per page configuration", CONFIG));
    }
}
