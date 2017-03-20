package com.commercetools.sunrise.search.facetedsearch;

import javax.annotation.Nullable;

public abstract class AbstractConfiguredFacetedSearchFormSettingsWithOptions extends AbstractConfiguredFacetedSearchFormSettings implements ConfiguredFacetedSearchFormSettingsWithOptions {

    private final boolean multiSelect;
    private final boolean matchingAll;

    protected AbstractConfiguredFacetedSearchFormSettingsWithOptions(final String fieldLabel, final String attributePath,
                                                                     final boolean isCountDisplayed, @Nullable final String uiType,
                                                                     final boolean multiSelect, final boolean matchingAll) {
        super(fieldLabel, attributePath, isCountDisplayed, uiType);
        this.multiSelect = multiSelect;
        this.matchingAll = matchingAll;
    }

    @Override
    public boolean isMultiSelect() {
        return multiSelect;
    }

    @Override
    public boolean isMatchingAll() {
        return matchingAll;
    }
}
