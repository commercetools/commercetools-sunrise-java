package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.commercetools.sunrise.core.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.categories.Category;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCategoryFetcher.class)
public interface CategoryFetcher extends ResourceFetcher {

    CompletionStage<Optional<Category>> get(final String categoryIdentifier);

    default CompletionStage<Category> require(final String categoryIdentifier) {
        return get(categoryIdentifier).thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
