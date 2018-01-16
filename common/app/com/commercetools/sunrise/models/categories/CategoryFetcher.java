package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.commercetools.sunrise.core.SingleResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCategoryFetcher.class)
public interface CategoryFetcher extends SingleResourceFetcher<Category, CategoryQuery> {

    Optional<CategoryQuery> defaultRequest(final String categoryIdentifier);

    CompletionStage<Optional<Category>> get(final String categoryIdentifier);

    default CompletionStage<Category> require(final String categoryIdentifier) {
        return get(categoryIdentifier).thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
