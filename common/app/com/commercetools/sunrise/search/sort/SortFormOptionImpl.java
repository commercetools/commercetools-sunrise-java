package com.commercetools.sunrise.search.sort;

import com.commercetools.sunrise.framework.SunriseModel;

import java.util.List;
import java.util.Locale;

import static com.commercetools.sunrise.search.SearchUtils.localizeExpression;
import static java.util.stream.Collectors.toList;

class SortFormOptionImpl extends SunriseModel implements SortFormOption {

    private final ConfiguredSortFormOption configuration;
    private final Locale locale;

    SortFormOptionImpl(final ConfiguredSortFormOption configuration, final Locale locale) {
        this.configuration = configuration;
        this.locale = locale;
    }

    @Override
    public ConfiguredSortFormOption configuration() {
        return configuration;
    }

    @Override
    public String getFieldLabel() {
        return configuration.getFieldLabel();
    }

    @Override
    public String getFieldValue() {
        return configuration.getFieldValue();
    }

    @Override
    public boolean isDefault() {
        return configuration.isDefault();
    }

    @Override
    public List<String> getValue() {
        return configuration.getValue().stream()
                .map(expr -> localizeExpression(expr, locale))
                .collect(toList());
    }
}
