package com.commercetools.sunrise.search.facetedsearch;

import com.commercetools.sunrise.framework.SunriseModel;

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
