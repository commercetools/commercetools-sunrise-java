package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.sessions.AbstractResourceInCache;
import io.sphere.sdk.categories.CategoryTree;
import play.cache.CacheApi;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class DefaultCachedCategoryTree extends AbstractResourceInCache<CategoryTree> implements CachedCategoryTree {

    private final CategoryTreeFetcher categoryTreeFetcher;

    @Inject
    DefaultCachedCategoryTree(final CacheApi cacheApi, final CategoryTreeFetcher categoryTreeFetcher) {
        super(cacheApi);
        this.categoryTreeFetcher = categoryTreeFetcher;
    }

    @Override
    protected String cacheKey() {
        return "category-tree";
    }

    @Override
    protected CompletionStage<CategoryTree> fetchResource() {
        return categoryTreeFetcher.get();
    }
}
