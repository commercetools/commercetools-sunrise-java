package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.HandlebarsHook;
import com.commercetools.sunrise.models.categories.CategorySettings;
import com.commercetools.sunrise.models.categories.CachedCategoryTree;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Resource;
import io.sphere.sdk.products.ProductProjection;
import play.cache.CacheApi;
import play.libs.concurrent.HttpExecution;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

public final class NewProductFlagComponent implements ControllerComponent, HandlebarsHook {

    private static final String CACHE_KEY = "new-product-flag-ids";

    private final CacheApi cacheApi;
    private final CachedCategoryTree cachedCategoryTree;
    @Nullable
    private final String newExtId;

    @Inject
    NewProductFlagComponent(final CacheApi cacheApi, final CachedCategoryTree cachedCategoryTree, final CategorySettings settings) {
        this.cacheApi = cacheApi;
        this.cachedCategoryTree = cachedCategoryTree;
        this.newExtId = settings.newExtId().orElse(null);
    }

    @Override
    public CompletionStage<Handlebars> onHandlebarsCreated(final Handlebars handlebars) {
        return getNewCategoryIds().thenApply(categoryIds ->
                handlebars.registerHelper("ifNewProduct", isNewHelper(categoryIds)));
    }

    private Helper<ProductProjection> isNewHelper(final List<String> categoryIds) {
        return (product, options) -> {
            final boolean isNew = product.getCategories().stream()
                    .anyMatch(categoryRef -> categoryIds.contains(categoryRef.getId()));
            return isNew ? options.fn() : null;
        };
    }

    private CompletionStage<List<String>> getNewCategoryIds() {
        if (newExtId != null) {
            final List<String> categoryIds = cacheApi.get(CACHE_KEY);
            return Optional.ofNullable(categoryIds)
                    .map(categoryTree -> (CompletionStage<List<String>>) completedFuture(categoryIds))
                    .orElseGet(() -> fetchAndStoreResource(newExtId));
        }
        return completedFuture(emptyList());
    }

    private CompletionStage<List<String>> fetchAndStoreResource(@Nonnull final String extId) {
        final CompletionStage<List<String>> categoryIdsStage = fetchResource(extId);
        categoryIdsStage.thenAcceptAsync(categoryIds -> cacheApi.set(CACHE_KEY, categoryIds), HttpExecution.defaultContext());
        return categoryIdsStage;
    }

    private CompletionStage<List<String>> fetchResource(@Nonnull final String extId) {
        return cachedCategoryTree.require()
                .thenApply(categoryTree -> categoryTree.findByExternalId(extId)
                        .map(categoryTree::findChildren)
                        .map(categoryTree::getSubtree)
                        .map(CategoryTree::getAllAsFlatList)
                        .map(categories -> categories.parallelStream().map(Resource::getId).collect(toList()))
                        .orElseGet(Collections::emptyList));
    }
}
