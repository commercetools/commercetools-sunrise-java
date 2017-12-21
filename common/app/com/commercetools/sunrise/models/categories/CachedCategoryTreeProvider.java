package com.commercetools.sunrise.models.categories;

import io.sphere.sdk.categories.CategoryTree;
import play.cache.CacheApi;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public final class CachedCategoryTreeProvider implements Provider<CategoryTree> {

    private final CacheApi cacheApi;
    private final CategorySettings categorySettings;
    private final CategoryTreeProvider categoryTreeProvider;

    @Inject
    CachedCategoryTreeProvider(final CacheApi cacheApi, final CategorySettings categorySettings,
                               final CategoryTreeProvider categoryTreeProvider) {
        this.cacheApi = cacheApi;
        this.categorySettings = categorySettings;
        this.categoryTreeProvider = categoryTreeProvider;
    }

    @Override
    public CategoryTree get() {
        return categorySettings.cacheExpiration()
                .map(expiration -> cacheApi.getOrElse(categorySettings.cacheKey(), categoryTreeProvider::get, expiration))
                .orElseGet(() -> cacheApi.getOrElse(categorySettings.cacheKey(), categoryTreeProvider::get));
    }
}