package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.sessions.ResourceInCache;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.SphereTimeoutException;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;

@ImplementedBy(DefaultCachedCategoryTree.class)
public interface CachedCategoryTree extends ResourceInCache<CategoryTree> {

    @Override
    CompletionStage<CategoryTree> get();

    default CategoryTree blockingGet() throws SphereTimeoutException {
        return blockingWait(get(), Duration.ofSeconds(2));
    }
}
