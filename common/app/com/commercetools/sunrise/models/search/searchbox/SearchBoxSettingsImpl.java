package com.commercetools.sunrise.models.search.searchbox;

import com.commercetools.sunrise.core.SunriseModel;

class SearchBoxSettingsImpl extends SunriseModel implements SearchBoxSettings {

    private final String fieldName;

    SearchBoxSettingsImpl(final String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}