package com.commercetools.sunrise.search.facetedsearch;

import com.commercetools.sunrise.framework.SunriseModel;

import javax.annotation.Nullable;

public abstract class AbstractConfiguredFacetedSearchFormSettings extends SunriseModel implements ConfiguredFacetedSearchFormSettings {


    private final String fieldLabel;
    private final String attributePath;
    private final boolean isCountDisplayed;
    @Nullable
    private final String uiType;

    protected AbstractConfiguredFacetedSearchFormSettings(final String fieldLabel, final String attributePath,
                                                          final boolean isCountDisplayed, @Nullable final String uiType) {

        this.fieldLabel = fieldLabel;
        this.attributePath = attributePath;
        this.isCountDisplayed = isCountDisplayed;
        this.uiType = uiType;
    }

    @Override
    public String getFieldLabel() {
        return fieldLabel;
    }

    @Override
    public String getAttributePath() {
        return attributePath;
    }

    @Override
    public boolean isCountDisplayed() {
        return isCountDisplayed;
    }

    @Nullable
    @Override
    public String getUIType() {
        return uiType;
    }
}
