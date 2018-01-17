package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCategoryTreeFetcher.class)
public interface CategoryTreeFetcher extends ResourceFetcher<CategoryTree, CategoryQuery> {

    Optional<CategoryQuery> defaultRequest();

    CompletionStage<CategoryTree> get();
}
