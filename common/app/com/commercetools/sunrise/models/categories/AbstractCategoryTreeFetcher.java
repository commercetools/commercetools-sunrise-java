package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.AbstractHookRunner;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static io.sphere.sdk.queries.QueryExecutionUtils.queryAll;

public abstract class AbstractCategoryTreeFetcher extends AbstractHookRunner<CategoryTree, CategoryQuery> implements CategoryTreeFetcher {

    private final SphereClient sphereClient;

    protected AbstractCategoryTreeFetcher(final HookRunner hookRunner, final SphereClient sphereClient) {
        super(hookRunner);
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<CategoryTree> get() {
        return runHook(buildRequest(), r -> queryAll(sphereClient, r).thenApply(CategoryTree::of));
    }

    @Override
    protected CompletionStage<CategoryTree> runHook(final CategoryQuery request, final Function<CategoryQuery, CompletionStage<CategoryTree>> execution) {
        return hookRunner().run(CategoryTreeFetcherHook.class, request, execution, h -> h::on);
    }

    protected abstract CategoryQuery buildRequest();
}
