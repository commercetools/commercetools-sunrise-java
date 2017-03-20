package com.commercetools.sunrise.search.pagination;

import com.commercetools.sunrise.framework.SunriseModel;
import play.Configuration;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class EntriesPerPageFormSettingsFactory extends SunriseModel {

    private static final String CONFIG_FIELD_NAME = "fieldName";
    private static final String DEFAULT_FIELD_NAME = "pp";
    private static final String CONFIG_OPTIONS = "options";

    private final EntriesPerPageFormOptionFactory entriesPerPageFormOptionFactory;

    @Inject
    public EntriesPerPageFormSettingsFactory(final EntriesPerPageFormOptionFactory entriesPerPageFormOptionFactory) {
        this.entriesPerPageFormOptionFactory = entriesPerPageFormOptionFactory;
    }

    public EntriesPerPageFormSettings create(final Configuration configuration) {
        final String fieldName = fieldName(configuration);
        final List<EntriesPerPageFormOption> options = options(configuration);
        return new EntriesPerPageFormSettingsImpl(fieldName, options);
    }

    protected final String fieldName(final Configuration configuration) {
        return configuration.getString(CONFIG_FIELD_NAME, DEFAULT_FIELD_NAME);
    }

    protected final List<EntriesPerPageFormOption> options(final Configuration configuration) {
        return configuration.getConfigList(CONFIG_OPTIONS, emptyList()).stream()
                .map(entriesPerPageFormOptionFactory::create)
                .collect(toList());
    }
}
