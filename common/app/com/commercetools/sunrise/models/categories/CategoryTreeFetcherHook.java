package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface CategoryTreeFetcherHook extends FilterHook {

    CompletionStage<CategoryTree> on(CategoryQuery request, Function<CategoryQuery, CompletionStage<CategoryTree>> nextComponent);
}
