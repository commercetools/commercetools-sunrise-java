package com.commercetools.sunrise.productcatalog.products.search.facetedsearch.categorytree;

import com.commercetools.sunrise.core.SunriseModel;
import com.commercetools.sunrise.models.categories.CachedCategoryTree;
import com.commercetools.sunrise.models.categories.CategorySettings;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.Locale;

public class CategoryTreeFacetedSearchFormSettingsFactory extends SunriseModel {

    private final Locale locale;
    private final CategorySettings categorySettings;
    private final CachedCategoryTree cachedCategoryTree;

    @Inject
    public CategoryTreeFacetedSearchFormSettingsFactory(final Locale locale, final CategorySettings categorySettings,
                                                        final CachedCategoryTree cachedCategoryTree) {
        this.locale = locale;
        this.categorySettings = categorySettings;
        this.cachedCategoryTree = cachedCategoryTree;
    }

    public CategoryTreeFacetedSearchFormSettings create(final ConfiguredCategoryTreeFacetedSearchFormSettings configuration) {
        final CategoryTree categoryTree = cachedCategoryTree.blockingGet();
        return new CategoryTreeFacetedSearchFormSettingsImpl(configuration, locale, categorySettings, categoryTree);
    }
}