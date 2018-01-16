package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.AbstractSingleResourceFetcher;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.CategoryLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CategoryQueryHook;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractCategoryFetcher extends AbstractSingleResourceFetcher<Category, CategoryQuery, PagedQueryResult<Category>> implements CategoryFetcher {

    protected AbstractCategoryFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Optional<Category>> get(final String categoryIdentifier) {
        return defaultRequest(categoryIdentifier)
                .map(request -> executeRequest(request, result -> selectResource(result, categoryIdentifier)))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected final CompletionStage<CategoryQuery> runRequestHook(final HookRunner hookRunner, final CategoryQuery baseRequest) {
        return CategoryQueryHook.runHook(hookRunner, baseRequest);
    }

    @Override
    protected final void runResourceLoadedHook(final HookRunner hookRunner, final Category resource) {
        CategoryLoadedHook.runHook(hookRunner, resource);
    }

    @Override
    protected final Optional<Category> selectResource(final PagedQueryResult<Category> pagedResult) {
        return super.selectResource(pagedResult);
    }

    protected Optional<Category> selectResource(final PagedQueryResult<Category> result, final String categoryIdentifier) {
        return selectResource(result);
    }
}
