package com.commercetools.sunrise.ctp.categories;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.queries.QueryExecutionUtils.queryAll;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

class CategoryTreeFilterImpl implements CategoryTreeFilter {

    private final CategoriesSettings categoriesSettings;
    private final SphereClient sphereClient;

    @Inject
    CategoryTreeFilterImpl(final CategoriesSettings categoriesSettings, final SphereClient sphereClient) {
        this.categoriesSettings = categoriesSettings;
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<CategoryTree> filter(final CategoryTree categoryTree) {
        if (categoriesSettings.discardEmpty()) {
            return fetchEmptyCategoryIds()
                    .thenApply(emptyCategoryIds -> discardEmptyCategories(categoryTree, emptyCategoryIds))
                    .thenApply(CategoryTree::of);
        }
        return completedFuture(categoryTree);
    }

    private CompletionStage<List<String>> fetchEmptyCategoryIds() {
        return queryAll(sphereClient, CategoriesWithProductCountQuery.of())
                .thenApply(categories -> categories.stream()
                        .filter(category -> !category.hasProducts())
                        .map(CategoryWithProductCount::getId)
                        .collect(toList()));
    }

    private List<Category> discardEmptyCategories(final CategoryTree categoryTree, final List<String> emptyCategoryIds) {
        return categoryTree.getAllAsFlatList().stream()
                .filter(category -> hasProducts(category, emptyCategoryIds))
                .collect(toList());
    }

    private boolean hasProducts(final Category category, final List<String> emptyCategoryIds) {
        return emptyCategoryIds.stream()
                .noneMatch(emptyCategoryId -> emptyCategoryId.equals(category.getId()));
    }
}
