package com.commercetools.sunrise.search.sort;

import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.commercetools.sunrise.framework.SunriseModel;
import play.Configuration;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class ConfiguredSortFormOptionFactory extends SunriseModel {

    private static final String CONFIG_FIELD_VALUE = "fieldValue";
    private static final String CONFIG_FIELD_LABEL = "fieldLabel";
    private static final String CONFIG_EXPRESSIONS = "expressions";
    private static final String CONFIG_DEFAULT = "default";

    public ConfiguredSortFormOption create(final Configuration configuration) {
        final String fieldLabel = fieldLabel(configuration);
        final String fieldValue = fieldValue(configuration);
        final List<String> expressions = expressions(configuration);
        final Boolean isDefault = isDefault(configuration);
        return new ConfiguredSortFormOptionImpl(fieldLabel, fieldValue, expressions, isDefault);
    }

    protected final String fieldLabel(final Configuration configuration) {
        return configuration.getString(CONFIG_FIELD_LABEL, "");
    }

    protected final String fieldValue(final Configuration configuration) {
        return Optional.ofNullable(configuration.getString(CONFIG_FIELD_VALUE))
                .orElseThrow(() -> new SunriseConfigurationException("Missing sort field value", CONFIG_FIELD_VALUE, configuration));
    }

    protected final List<String> expressions(final Configuration configuration) {
        return configuration.getStringList(CONFIG_EXPRESSIONS, emptyList()).stream()
                .filter(expr -> !expr.isEmpty())
                .collect(toList());
    }

    protected final Boolean isDefault(final Configuration configuration) {
        return configuration.getBoolean(CONFIG_DEFAULT, false);
    }
}
