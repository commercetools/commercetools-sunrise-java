package com.commercetools.sunrise.search.searchbox;

import com.commercetools.sunrise.framework.SunriseModel;
import play.Configuration;

public class SearchBoxSettingsFactory extends SunriseModel {

    private static final String CONFIG_FIELD_NAME = "fieldName";
    private static final String DEFAULT_FIELD_NAME = "q";

    public SearchBoxSettings create(final Configuration configuration) {
        final String fieldName = fieldName(configuration);
        return new SearchBoxSettingsImpl(fieldName);
    }

    protected final String fieldName(final Configuration configuration) {
        return configuration.getString(CONFIG_FIELD_NAME, DEFAULT_FIELD_NAME);
    }
}