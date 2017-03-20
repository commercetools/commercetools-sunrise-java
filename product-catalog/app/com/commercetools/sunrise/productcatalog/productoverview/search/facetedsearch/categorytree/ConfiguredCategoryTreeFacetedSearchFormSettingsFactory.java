package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree;

import com.commercetools.sunrise.search.facetedsearch.AbstractConfiguredFacetedSearchFormSettingsFactory;
import play.Configuration;

public class ConfiguredCategoryTreeFacetedSearchFormSettingsFactory extends AbstractConfiguredFacetedSearchFormSettingsFactory {

    public ConfiguredCategoryTreeFacetedSearchFormSettings create(final Configuration configuration) {
        final String fieldName = fieldName(configuration);
        final String fieldLabel = fieldLabel(configuration);
        final String attributePath = attributePath(configuration);
        final boolean countDisplayed = isCountDisplayed(configuration);
        final String uiType = uiType(configuration);
        return new ConfiguredCategoryTreeFacetedSearchFormSettingsImpl(fieldName, fieldLabel, attributePath, countDisplayed, uiType);
    }
}
