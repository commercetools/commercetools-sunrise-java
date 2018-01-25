package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.AbstractHookRunner;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractCategoryFetcher extends AbstractHookRunner<Optional<Category>, CategoryQuery> implements CategoryFetcher {

    private final SphereClient sphereClient;

    protected AbstractCategoryFetcher(final HookRunner hookRunner, final SphereClient sphereClient) {
        super(hookRunner);
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<Optional<Category>> get(final String categoryIdentifier) {
        return buildRequest(categoryIdentifier)
                .map(request -> runHook(request, r -> sphereClient.execute(r)
                        .thenApply(results -> selectResult(results, categoryIdentifier))))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected CompletionStage<Optional<Category>> runHook(final CategoryQuery request, final Function<CategoryQuery, CompletionStage<Optional<Category>>> execution) {
        return hookRunner().run(CategoryFetcherHook.class, request, execution, h -> h::on);
    }

    protected abstract Optional<CategoryQuery> buildRequest(String categoryIdentifier);

    protected Optional<Category> selectResult(final PagedQueryResult<Category> results, final String categoryIdentifier) {
        return results.head();
    }
}
