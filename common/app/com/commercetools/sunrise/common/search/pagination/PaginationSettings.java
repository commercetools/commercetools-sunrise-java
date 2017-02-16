package com.commercetools.sunrise.common.search.pagination;

import com.commercetools.sunrise.common.forms.FormSettings;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class PaginationSettings extends FormSettings<Integer> {

    private static final String CONFIG_KEY = "pop.pagination.key";

    private static final int DEFAULT_PAGE = 1;
    private static final String DEFAULT_KEY = "page";

    @Inject
    PaginationSettings(final Configuration configuration) {
        super(configuration.getString(CONFIG_KEY, DEFAULT_KEY), DEFAULT_PAGE);
    }

    @Override
    public String getFieldName() {
        return super.getFieldName();
    }

    @Override
    public Integer getDefaultValue() {
        return super.getDefaultValue();
    }

    @Override
    public Integer mapToValue(final String valueAsString) {
        try {
            return Integer.valueOf(valueAsString);
        } catch (NumberFormatException e) {
            return getDefaultValue();
        }
    }

    @Override
    public boolean isValidValue(final Integer value) {
        return value != null && value > 0;
    }
}