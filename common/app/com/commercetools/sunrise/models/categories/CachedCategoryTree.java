package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.sessions.ResourceInCache;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.categories.CategoryTree;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCachedCategoryTree.class)
public interface CachedCategoryTree extends ResourceInCache<CategoryTree> {

    @Override
    CompletionStage<Optional<CategoryTree>> get();
}
