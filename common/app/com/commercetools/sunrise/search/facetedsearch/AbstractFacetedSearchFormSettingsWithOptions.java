package com.commercetools.sunrise.search.facetedsearch;

import java.util.Locale;

public abstract class AbstractFacetedSearchFormSettingsWithOptions<C extends ConfiguredFacetedSearchFormSettingsWithOptions> extends AbstractFacetedSearchFormSettings<C> implements ConfiguredFacetedSearchFormSettingsWithOptions {

    protected AbstractFacetedSearchFormSettingsWithOptions(final C configuration, final Locale locale) {
        super(configuration, locale);
    }

    public boolean isMultiSelect() {
        return configuration().isMultiSelect();
    }

    public boolean isMatchingAll() {
        return configuration().isMatchingAll();
    }
}
