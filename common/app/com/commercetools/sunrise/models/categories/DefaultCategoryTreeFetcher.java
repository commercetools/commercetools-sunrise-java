package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.QuerySort;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

final class DefaultCategoryTreeFetcher extends AbstractCategoryTreeFetcher {

    private final List<String> sortExpressions;

    @Inject
    DefaultCategoryTreeFetcher(final SphereClient sphereClient, final HookRunner hookRunner, final CategorySettings settings) {
        super(sphereClient, hookRunner);
        this.sortExpressions = settings.sortExpressions();

    }

    @Override
    public Optional<CategoryQuery> defaultRequest() {
        return Optional.of(CategoryQuery.of().plusSort(sortExpressions()));
    }

    private List<QuerySort<Category>> sortExpressions() {
        return sortExpressions.stream()
                .map(QuerySort::<Category>of)
                .collect(toList());
    }
}
