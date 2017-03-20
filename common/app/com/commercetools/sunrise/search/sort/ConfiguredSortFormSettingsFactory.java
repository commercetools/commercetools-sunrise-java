package com.commercetools.sunrise.search.sort;

import com.commercetools.sunrise.framework.SunriseModel;
import play.Configuration;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class ConfiguredSortFormSettingsFactory extends SunriseModel {

    private static final String CONFIG_FIELD_NAME = "fieldName";
    private static final String DEFAULT_FIELD_NAME = "sort";
    private static final String CONFIG_OPTIONS = "options";

    private final ConfiguredSortFormOptionFactory configuredSortFormOptionFactory;

    @Inject
    public ConfiguredSortFormSettingsFactory(final ConfiguredSortFormOptionFactory configuredSortFormOptionFactory) {
        this.configuredSortFormOptionFactory = configuredSortFormOptionFactory;
    }

    public ConfiguredSortFormSettings create(final Configuration configuration) {
        final String fieldName = fieldName(configuration);
        final List<ConfiguredSortFormOption> options = options(configuration);
        return new ConfiguredSortFormSettingsImpl(fieldName, options);
    }

    protected final String fieldName(final Configuration configuration) {
        return configuration.getString(CONFIG_FIELD_NAME, DEFAULT_FIELD_NAME);
    }

    protected final List<ConfiguredSortFormOption> options(final Configuration configuration) {
        return configuration.getConfigList(CONFIG_OPTIONS, emptyList()).stream()
                .map(configuredSortFormOptionFactory::create)
                .collect(toList());
    }
}
