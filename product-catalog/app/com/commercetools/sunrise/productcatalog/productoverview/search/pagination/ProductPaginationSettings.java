package com.commercetools.sunrise.productcatalog.productoverview.search.pagination;

import com.commercetools.sunrise.framework.SunriseConfigurationException;
import com.commercetools.sunrise.search.pagination.PaginationSettings;
import com.commercetools.sunrise.search.pagination.PaginationSettingsFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public final class ProductPaginationSettings implements PaginationSettings {

    private static final String CONFIG = "pop.pagination";

    private final PaginationSettings settings;

    @Inject
    ProductPaginationSettings(final Configuration configuration,
                              final PaginationSettingsFactory paginationSettingsFactory) {
        this.settings = paginationSettingsFactory.create(configuration(configuration));
    }

    @Override
    public Long getDefaultValue() {
        return settings.getDefaultValue();
    }

    @Override
    public String getFieldName() {
        return settings.getFieldName();
    }

    @Override
    public int getDisplayedPages() {
        return settings.getDisplayedPages();
    }

    private static Configuration configuration(final Configuration configuration) {
        return Optional.ofNullable(configuration.getConfig(CONFIG))
                .orElseThrow(() -> new SunriseConfigurationException("Could not find product pagination configuration", CONFIG));
    }
}