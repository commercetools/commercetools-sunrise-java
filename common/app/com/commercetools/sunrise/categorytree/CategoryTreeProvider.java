package com.commercetools.sunrise.categorytree;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.QuerySort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;
import static io.sphere.sdk.queries.QueryExecutionUtils.queryAll;
import static java.util.stream.Collectors.toList;

public final class CategoryTreeProvider implements Provider<CategoryTree> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryTreeProvider.class);

    private final CategoryTreeConfiguration configuration;
    private final CategoryTreeFilter categoryTreeFilter;
    private final SphereClient sphereClient;

    @Inject
    CategoryTreeProvider(final CategoryTreeConfiguration configuration, final CategoryTreeFilter categoryTreeFilter,
                         final SphereClient sphereClient) {
        this.categoryTreeFilter = categoryTreeFilter;
        this.configuration = configuration;
        this.sphereClient = sphereClient;
    }

    @Override
    public CategoryTree get() {
        final CategoryTree categoryTree = blockingWait(createCategoryTree(), Duration.ofSeconds(30));
        LOGGER.info("Fetched category tree with " + categoryTree.getAllAsFlatList().size() + " categories");
        return categoryTree;
    }

    private CompletionStage<CategoryTree> createCategoryTree() {
        return queryAll(sphereClient, buildQuery())
                .thenApply(CategoryTree::of)
                .thenComposeAsync(categoryTreeFilter::filter, HttpExecution.defaultContext());
    }

    private CategoryQuery buildQuery() {
        final List<QuerySort<Category>> sortExpressions = configuration.sortExpressions().stream()
                .map(QuerySort::<Category>of)
                .collect(toList());
        return CategoryQuery.of()
                .withSort(sortExpressions);
    }
}
