package com.commercetools.sunrise.common.search.searchbox;

import com.commercetools.sunrise.common.forms.FormSettings;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class SearchBoxSettings extends FormSettings<String> {

    private static final String CONFIG_KEY = "pop.searchTerm.key";
    private static final String DEFAULT_KEY = "q";

    @Inject
    public SearchBoxSettings(final Configuration configuration) {
        super(configuration.getString(CONFIG_KEY, DEFAULT_KEY), "");
    }

    @Override
    public String getFieldName() {
        return super.getFieldName();
    }

    @Override
    public String getDefaultValue() {
        return super.getDefaultValue();
    }

    @Override
    public String mapToValue(final String optionValueAsString) {
        return optionValueAsString;
    }
}