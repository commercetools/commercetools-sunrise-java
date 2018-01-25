package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface CategoryFetcherHook extends FilterHook {

    CompletionStage<Optional<Category>> on(CategoryQuery request, Function<CategoryQuery, CompletionStage<Optional<Category>>> nextComponent);
}
