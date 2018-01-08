package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree;

import com.commercetools.sunrise.models.categories.CategorySettings;
import com.commercetools.sunrise.core.SunriseModel;
import com.commercetools.sunrise.models.categories.CategoryFinder;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.Locale;

public class CategoryTreeFacetedSearchFormSettingsFactory extends SunriseModel {

    private final Locale locale;
    private final CategoryFinder categoryFinder;
    private final CategorySettings categorySettings;
    private final CategoryTree categoryTree;

    @Inject
    public CategoryTreeFacetedSearchFormSettingsFactory(final Locale locale, final CategoryFinder categoryFinder,
                                                        final CategorySettings categorySettings,
                                                        final CategoryTree categoryTree) {
        this.locale = locale;
        this.categoryFinder = categoryFinder;
        this.categorySettings = categorySettings;
        this.categoryTree = categoryTree;
    }

    public CategoryTreeFacetedSearchFormSettings create(final ConfiguredCategoryTreeFacetedSearchFormSettings configuration) {
        return new CategoryTreeFacetedSearchFormSettingsImpl(configuration, locale, categoryFinder, categorySettings, categoryTree);
    }
}