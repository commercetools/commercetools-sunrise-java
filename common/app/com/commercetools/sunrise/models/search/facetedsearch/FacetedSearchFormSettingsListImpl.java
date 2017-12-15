package com.commercetools.sunrise.models.search.facetedsearch;

import com.commercetools.sunrise.core.SunriseModel;

import java.util.List;

final class FacetedSearchFormSettingsListImpl<T> extends SunriseModel implements FacetedSearchFormSettingsList<T> {

    private final List<? extends FacetedSearchFormSettings<T>> settings;

    FacetedSearchFormSettingsListImpl(final List<? extends FacetedSearchFormSettings<T>> settings) {
        this.settings = settings;
    }

    @Override
    public List<? extends FacetedSearchFormSettings<T>> getSettings() {
        return settings;
    }
}
