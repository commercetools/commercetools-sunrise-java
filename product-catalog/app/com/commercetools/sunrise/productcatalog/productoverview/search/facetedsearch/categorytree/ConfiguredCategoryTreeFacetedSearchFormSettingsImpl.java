package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree;

import com.commercetools.sunrise.search.facetedsearch.AbstractConfiguredFacetedSearchFormSettings;

import javax.annotation.Nullable;
import javax.inject.Inject;

final class ConfiguredCategoryTreeFacetedSearchFormSettingsImpl extends AbstractConfiguredFacetedSearchFormSettings implements ConfiguredCategoryTreeFacetedSearchFormSettings {

    private final String fieldName;

    @Inject
    public ConfiguredCategoryTreeFacetedSearchFormSettingsImpl(final String fieldName, final String fieldLabel, final String attributePath,
                                                               final boolean countDisplayed, @Nullable final String uiType) {
        super(fieldLabel, attributePath, countDisplayed, uiType);
        this.fieldName = fieldName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
