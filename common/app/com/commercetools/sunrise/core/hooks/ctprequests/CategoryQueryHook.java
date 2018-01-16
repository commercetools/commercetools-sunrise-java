package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.categories.queries.CategoryQuery;

import java.util.concurrent.CompletionStage;

public interface CategoryQueryHook extends CtpRequestHook {

    CompletionStage<CategoryQuery> onCategoryQuery(final CategoryQuery query);

    static CompletionStage<CategoryQuery> runHook(final HookRunner hookRunner, final CategoryQuery query) {
        return hookRunner.runActionHook(CategoryQueryHook.class, CategoryQueryHook::onCategoryQuery, query);
    }
}
