package com.commercetools.sunrise.ctp.categories;

import io.sphere.sdk.categories.CategoryTree;
import play.cache.CacheApi;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public final class CachedCategoryTreeProvider implements Provider<CategoryTree> {

    private final CacheApi cacheApi;
    private final CategoriesSettings categoriesSettings;
    private final CategoryTreeProvider categoryTreeProvider;

    @Inject
    CachedCategoryTreeProvider(final CacheApi cacheApi, final CategoriesSettings categoriesSettings,
                               final CategoryTreeProvider categoryTreeProvider) {
        this.cacheApi = cacheApi;
        this.categoriesSettings = categoriesSettings;
        this.categoryTreeProvider = categoryTreeProvider;
    }

    @Override
    public CategoryTree get() {
        return categoriesSettings.cacheExpiration()
                .map(expiration -> cacheApi.getOrElse(categoriesSettings.cacheKey(), categoryTreeProvider::get, expiration))
                .orElseGet(() -> cacheApi.getOrElse(categoriesSettings.cacheKey(), categoryTreeProvider::get));
    }
}