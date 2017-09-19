package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree;

import com.commercetools.sunrise.ctp.categories.CategoriesSettings;
import com.commercetools.sunrise.framework.SunriseModel;
import com.commercetools.sunrise.productcatalog.productoverview.CategoryFinder;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.Locale;

public class CategoryTreeFacetedSearchFormSettingsFactory extends SunriseModel {

    private final Locale locale;
    private final CategoryFinder categoryFinder;
    private final CategoriesSettings categoriesSettings;
    private final CategoryTree categoryTree;

    @Inject
    public CategoryTreeFacetedSearchFormSettingsFactory(final Locale locale, final CategoryFinder categoryFinder,
                                                        final CategoriesSettings categoriesSettings,
                                                        final CategoryTree categoryTree) {
        this.locale = locale;
        this.categoryFinder = categoryFinder;
        this.categoriesSettings = categoriesSettings;
        this.categoryTree = categoryTree;
    }

    public CategoryTreeFacetedSearchFormSettings create(final ConfiguredCategoryTreeFacetedSearchFormSettings configuration) {
        return new CategoryTreeFacetedSearchFormSettingsImpl(configuration, locale, categoryFinder, categoriesSettings, categoryTree);
    }
}