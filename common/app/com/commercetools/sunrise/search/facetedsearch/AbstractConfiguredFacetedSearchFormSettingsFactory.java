package com.commercetools.sunrise.search.facetedsearch;

import com.commercetools.sunrise.framework.SunriseConfigurationException;
import com.commercetools.sunrise.framework.SunriseModel;
import play.Configuration;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AbstractConfiguredFacetedSearchFormSettingsFactory extends SunriseModel {

    private static final String CONFIG_FIELD_NAME = "fieldName";
    private static final String CONFIG_FIELD_LABEL = "fieldLabel";
    private static final String CONFIG_UI_TYPE = "uiType";
    private static final String CONFIG_ATTRIBUTE_PATH = "attributePath";
    private static final String CONFIG_COUNT = "count";

    protected final String fieldName(final Configuration configuration) {
        return Optional.ofNullable(configuration.getString(CONFIG_FIELD_NAME))
                .orElseThrow(() -> new SunriseConfigurationException("Missing required field name for facet", CONFIG_FIELD_NAME, configuration));
    }

    protected final String fieldLabel(final Configuration configuration) {
        return configuration.getString(CONFIG_FIELD_LABEL, "");
    }

    protected final String attributePath(final Configuration configuration) {
        return Optional.ofNullable(configuration.getString(CONFIG_ATTRIBUTE_PATH))
                .orElseThrow(() -> new SunriseConfigurationException("Missing facet attribute path expression", CONFIG_ATTRIBUTE_PATH, configuration));
    }

    protected final boolean isCountDisplayed(final Configuration configuration) {
        return configuration.getBoolean(CONFIG_COUNT, true);
    }

    @Nullable
    protected final String uiType(final Configuration configuration) {
        return configuration.getString(CONFIG_UI_TYPE);
    }
}
