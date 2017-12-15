package com.commercetools.sunrise.models.search.facetedsearch.terms;

import com.commercetools.sunrise.core.SunriseModel;
import com.commercetools.sunrise.core.injection.RequestScoped;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class TermFacetedSearchFormSettingsFactory extends SunriseModel {

    private final Locale locale;

    @Inject
    public TermFacetedSearchFormSettingsFactory(final Locale locale) {
        this.locale = locale;
    }

    public <T> TermFacetedSearchFormSettings<T> create(final ConfiguredTermFacetedSearchFormSettings configuration) {
        return new TermFacetedSearchFormSettingsImpl<>(configuration, locale);
    }
}
