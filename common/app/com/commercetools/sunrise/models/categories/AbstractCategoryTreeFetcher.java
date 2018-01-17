package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.CategoryTreeLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CategoryQueryHook;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.queries.QueryExecutionUtils.queryAll;
import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractCategoryTreeFetcher extends AbstractSphereRequestExecutor implements CategoryTreeFetcher {

    protected AbstractCategoryTreeFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<CategoryTree> get() {
        return defaultRequest().map(this::executeRequest).orElseGet(() -> completedFuture(CategoryTree.of(emptyList())));
    }

    @Override
    public CompletionStage<CategoryTree> get(final CategoryQuery request) {
        return executeRequest(request);
    }

    protected final CompletionStage<CategoryTree> executeRequest(final CategoryQuery baseRequest) {
        return runRequestHook(getHookRunner(), baseRequest).thenCompose(request -> {
            final CompletionStage<CategoryTree> resultStage = queryAll(getSphereClient(), request).thenApply(CategoryTree::of);
            resultStage.thenAcceptAsync(result -> runResourceLoadedHook(getHookRunner(), result), HttpExecution.defaultContext());
            return resultStage;
        });
    }

    protected final CompletionStage<CategoryQuery> runRequestHook(final HookRunner hookRunner, final CategoryQuery baseRequest) {
        return CategoryQueryHook.runHook(hookRunner, baseRequest);
    }

    protected final void runResourceLoadedHook(final HookRunner hookRunner, final CategoryTree categoryTree) {
        CategoryTreeLoadedHook.runHook(hookRunner, categoryTree);
    }
}
