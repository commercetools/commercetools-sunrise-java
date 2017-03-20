package com.commercetools.sunrise.search.sort;

import com.commercetools.sunrise.framework.SunriseModel;

import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

final class SortFormSettingsImpl<T> extends SunriseModel implements SortFormSettings<T> {

    private final ConfiguredSortFormSettings configuration;
    private final List<SortFormOption> options;

    SortFormSettingsImpl(final ConfiguredSortFormSettings configuration, final Locale locale) {
        this.configuration = configuration;
        this.options = configuration.getOptions().stream()
                .map(option -> new SortFormOptionImpl(option, locale))
                .collect(toList());
    }

    @Override
    public ConfiguredSortFormSettings configuration() {
        return configuration;
    }

    @Override
    public List<SortFormOption> getOptions() {
        return options;
    }
}
