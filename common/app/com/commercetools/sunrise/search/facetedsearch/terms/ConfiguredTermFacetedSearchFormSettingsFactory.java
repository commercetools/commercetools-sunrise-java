package com.commercetools.sunrise.search.facetedsearch.terms;

import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.commercetools.sunrise.search.facetedsearch.AbstractConfiguredFacetedSearchFormSettingsWithOptionsFactory;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class ConfiguredTermFacetedSearchFormSettingsFactory extends AbstractConfiguredFacetedSearchFormSettingsWithOptionsFactory {

    private static final String CONFIG_MAPPER = "mapper";
    private static final String CONFIG_MAPPER_NAME = "type";
    private static final String CONFIG_MAPPER_VALUES = "values";
    private static final String CONFIG_LIMIT = "limit";
    private static final String CONFIG_THRESHOLD = "threshold";

    public ConfiguredTermFacetedSearchFormSettings create(final Configuration configuration) {
        final String fieldName = fieldName(configuration);
        final String fieldLabel = fieldLabel(configuration);
        final String attributePath = attributePath(configuration);
        final boolean countDisplayed = isCountDisplayed(configuration);
        final String uiType = uiType(configuration);
        final boolean multiSelect = isMultiSelect(configuration);
        final boolean matchingAll = isMatchingAll(configuration);
        final TermFacetMapperSettings mapperSettings = mapperSettings(configuration);
        final Long limit = limit(configuration);
        final Long threshold = threshold(configuration);
        return new ConfiguredTermFacetedSearchFormSettingsImpl(fieldName, fieldLabel, attributePath, countDisplayed, uiType, multiSelect, matchingAll, mapperSettings, limit, threshold);
    }

    @Nullable
    protected final TermFacetMapperSettings mapperSettings(final Configuration configuration) {
        return Optional.ofNullable(configuration.getConfig(CONFIG_MAPPER))
                .map(mapperConfig -> TermFacetMapperSettings.of(mapperName(mapperConfig), mapperConfig.getStringList(CONFIG_MAPPER_VALUES)))
                .orElse(null);
    }

    protected final String mapperName(final Configuration configuration) {
        return Optional.ofNullable(configuration.getString(CONFIG_MAPPER_NAME))
                .orElseThrow(() -> new SunriseConfigurationException("Could not find required type for the facet term mapper", CONFIG_MAPPER_NAME, configuration));
    }

    @Nullable
    protected final Long limit(final Configuration configuration) {
        return configuration.getLong(CONFIG_LIMIT);
    }

    @Nullable
    protected final Long threshold(final Configuration configuration) {
        return configuration.getLong(CONFIG_THRESHOLD);
    }
}
