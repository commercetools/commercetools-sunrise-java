package com.commercetools.sunrise.search.facetedsearch.sliderranges;

import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.commercetools.sunrise.search.facetedsearch.AbstractConfiguredFacetedSearchFormSettingsFactory;
import play.Configuration;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class ConfiguredSliderRangeFacetedSearchFormSettingsFactory extends AbstractConfiguredFacetedSearchFormSettingsFactory {

    private static final String CONFIG_LOWER = "lowerEndpoint";
    private static final String CONFIG_UPPER = "upperEndpoint";
    private static final String CONFIG_ENDPOINT_FIELD_NAME = "fieldName";

    public ConfiguredSliderRangeFacetedSearchFormSettings create(final Configuration configuration) {
        final String fieldLabel = fieldLabel(configuration);
        final String attributePath = attributePath(configuration);
        final boolean countDisplayed = isCountDisplayed(configuration);
        final String uiType = uiType(configuration);
        final RangeEndpointFormSettings lowerEndpointSettings = lowerEndpointSettings(configuration);
        final RangeEndpointFormSettings upperEndpointSettings = upperEndpointSettings(configuration);
        return new ConfiguredSliderRangeFacetedSearchFormSettingsImpl(fieldLabel, attributePath, countDisplayed, uiType, lowerEndpointSettings, upperEndpointSettings);
    }

    protected final RangeEndpointFormSettings lowerEndpointSettings(final Configuration configuration) {
        return endpointSettings(Optional.ofNullable(configuration.getConfig(CONFIG_LOWER))
                .orElseThrow(() -> new SunriseConfigurationException("Missing required lower endpoint configuration", CONFIG_LOWER, configuration)));
    }

    protected final RangeEndpointFormSettings upperEndpointSettings(final Configuration configuration) {
        return endpointSettings(Optional.ofNullable(configuration.getConfig(CONFIG_UPPER))
                .orElseThrow(() -> new SunriseConfigurationException("Missing required upper endpoint configuration", CONFIG_UPPER, configuration)));
    }

    private static RangeEndpointFormSettings endpointSettings(final Configuration configuration) {
        final String fieldName = Optional.ofNullable(configuration.getString(CONFIG_ENDPOINT_FIELD_NAME))
                .orElseThrow(() -> new SunriseConfigurationException("Missing required field name for slider range facet endpoint", CONFIG_ENDPOINT_FIELD_NAME, configuration));
        return RangeEndpointFormSettings.of(fieldName);
    }
}
