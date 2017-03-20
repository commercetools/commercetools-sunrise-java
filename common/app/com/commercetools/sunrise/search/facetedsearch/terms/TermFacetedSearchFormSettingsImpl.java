package com.commercetools.sunrise.search.facetedsearch.terms;

import com.commercetools.sunrise.search.facetedsearch.AbstractFacetedSearchFormSettingsWithOptions;

import javax.annotation.Nullable;
import java.util.Locale;

final class TermFacetedSearchFormSettingsImpl<T> extends AbstractFacetedSearchFormSettingsWithOptions<ConfiguredTermFacetedSearchFormSettings> implements TermFacetedSearchFormSettings<T> {

    TermFacetedSearchFormSettingsImpl(final ConfiguredTermFacetedSearchFormSettings configuration, final Locale locale) {
        super(configuration, locale);
    }

    @Override
    public String getFieldName() {
        return configuration().getFieldName();
    }

    @Nullable
    @Override
    public Long getThreshold() {
        return configuration().getThreshold();
    }

    @Nullable
    @Override
    public Long getLimit() {
        return configuration().getLimit();
    }

    @Nullable
    @Override
    public TermFacetMapperSettings getMapperSettings() {
        return configuration().getMapperSettings();
    }
}
