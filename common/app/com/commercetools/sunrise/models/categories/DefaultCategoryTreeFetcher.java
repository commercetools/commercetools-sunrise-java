package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.QuerySort;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

public final class DefaultCategoryTreeFetcher extends AbstractCategoryTreeFetcher {

    private final List<String> sortExpressions;

    @Inject
    DefaultCategoryTreeFetcher(final HookRunner hookRunner, final SphereClient sphereClient,
                               final CategorySettings settings) {
        super(hookRunner, sphereClient);
        this.sortExpressions = settings.sortExpressions();
    }

    @Override
    protected CategoryQuery buildRequest() {
        return CategoryQuery.of().plusSort(sortExpressions());
    }

    private List<QuerySort<Category>> sortExpressions() {
        return sortExpressions.stream()
                .map(QuerySort::<Category>of)
                .collect(toList());
    }
}
