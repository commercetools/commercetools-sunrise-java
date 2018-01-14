package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.HttpRequestStartedHook;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.queries.PagedQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;

public final class HomeSuggestionsComponent implements ControllerComponent, HttpRequestStartedHook, PageDataHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeSuggestionsComponent.class);

    private final int numSuggestions;
    private final List<String> categoryIds;
    private final SphereClient sphereClient;
    private final PriceSelection priceSelection;

    private CompletionStage<PagedQueryResult<ProductProjection>> suggestionsStage;

    @Inject
    public HomeSuggestionsComponent(final Configuration configuration, final CategoryTree categoryTree,
                                    final SphereClient sphereClient, final PriceSelection priceSelection) {
        this.numSuggestions = configuration.getInt("homeSuggestions.count", 4);
        this.categoryIds = configuration.getStringList("homeSuggestions.externalId", emptyList()).stream()
                .map(categoryTree::findByExternalId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Category::getId)
                .collect(Collectors.toList());
        this.sphereClient = sphereClient;
        this.priceSelection = priceSelection;
    }


    @Override
    public void onHttpRequestStarted(final Http.Context context) {
        this.suggestionsStage = fetchRelatedProducts(categoryIds, numSuggestions);
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
     * @param numProducts the number of products the returned result should contain
     * @return the products related to these categories
     */
    private CompletionStage<PagedQueryResult<ProductProjection>> fetchRelatedProducts(final List<String> categoryIds, final int numProducts) {
        if (categoryIds.isEmpty()) {
            return completedFuture(PagedQueryResult.empty());
        } else {
            return sphereClient.execute(ProductProjectionQuery.ofCurrent()
                    .withPredicates(product -> product.categories().id().isIn(categoryIds))
                    .withPriceSelection(priceSelection)
                    .withLimit(numProducts));
        }
    }
}
