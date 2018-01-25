package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.HttpRequestHook;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.categories.CachedCategoryTree;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Resource;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.queries.PagedQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;
import play.cache.CacheApi;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

public final class HomeSuggestionsComponent implements ControllerComponent, HttpRequestHook, PageDataHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeSuggestionsComponent.class);
    private static final String CACHE_KEY = "home-suggestions-ids";

    private final CacheApi cacheApi;
    private final CachedCategoryTree cachedCategoryTree;
    private final SphereClient sphereClient;
    private final PriceSelection priceSelection;
    private final List<String> categoryExternalIds;
    private final int numSuggestions;

    private CompletionStage<PagedQueryResult<ProductProjection>> suggestionsStage;

    @Inject
    public HomeSuggestionsComponent(final Configuration configuration, final CacheApi cacheApi,
                                    final CachedCategoryTree cachedCategoryTree, final SphereClient sphereClient,
                                    final PriceSelection priceSelection) {
        this.numSuggestions = configuration.getInt("homeSuggestions.count", 4);
        this.categoryExternalIds = configuration.getStringList("homeSuggestions.externalId", emptyList());
        this.cacheApi = cacheApi;
        this.cachedCategoryTree = cachedCategoryTree;
        this.sphereClient = sphereClient;
        this.priceSelection = priceSelection;
    }

    @Override
    public CompletionStage<Result> on(final Http.Context ctx, final Function<Http.Context, CompletionStage<Result>> nextComponent) {
        this.suggestionsStage = getSuggestionsCategoryIds().thenCompose(this::fetchRelatedProducts);
        return nextComponent.apply(ctx);
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        if (suggestionsStage != null) {
            return suggestionsStage.thenApply(products -> pageData.put("suggestions", products))
                    .exceptionally(throwable -> {
                        LOGGER.error("Failed to fetch suggested products", throwable);
                        return pageData;
                    });
        }
        return completedFuture(pageData);
    }

    /**
     * Gets products belonging to any of the given categories.
     * @param categoryIds the categories to get suggestions from
     * @return the products related to these categories
     */
    private CompletionStage<PagedQueryResult<ProductProjection>> fetchRelatedProducts(final List<String> categoryIds) {
        if (categoryIds.isEmpty()) {
            return completedFuture(PagedQueryResult.empty());
        } else {
            return sphereClient.execute(ProductProjectionQuery.ofCurrent()
                    .withPredicates(product -> product.categories().id().isIn(categoryIds))
                    .withPriceSelection(priceSelection)
                    .withLimit(numSuggestions));
        }
    }

    private CompletionStage<List<String>> getSuggestionsCategoryIds() {
        if (!categoryExternalIds.isEmpty()) {
            final List<String> nullableCategoryIds = cacheApi.get(CACHE_KEY);
            return Optional.ofNullable(nullableCategoryIds)
                    .map(categoryIds -> (CompletionStage<List<String>>) completedFuture(categoryIds))
                    .orElseGet(this::fetchAndStoreResource);
        }
        return completedFuture(emptyList());
    }

    private CompletionStage<List<String>> fetchAndStoreResource() {
        final CompletionStage<List<String>> categoryIdsStage = fetchResource();
        categoryIdsStage.thenAcceptAsync(categoryIds -> cacheApi.set(CACHE_KEY, categoryIds), HttpExecution.defaultContext());
        return categoryIdsStage;
    }

    private CompletionStage<List<String>> fetchResource() {
        return cachedCategoryTree.get()
                .thenApply(categoryTree -> categoryExternalIds.parallelStream()
                        .map(externalId -> categoryTree.findByExternalId(externalId).map(Resource::getId))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(toList()));
    }
}
