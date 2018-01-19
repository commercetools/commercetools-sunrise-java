package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface CategoryQueryHook extends FilterHook {

    CompletionStage<PagedQueryResult<Category>> on(CategoryQuery request, Function<CategoryQuery, CompletionStage<PagedQueryResult<Category>>> nextComponent);

    static CompletionStage<PagedQueryResult<Category>> run(final HookRunner hookRunner, final CategoryQuery request, final Function<CategoryQuery, CompletionStage<PagedQueryResult<Category>>> execution) {
        return hookRunner.run(CategoryQueryHook.class, request, execution, h -> h::on);
    }
}
