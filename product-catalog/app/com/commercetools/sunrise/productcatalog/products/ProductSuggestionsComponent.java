package com.commercetools.sunrise.productcatalog.products;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ProductProjectionLoadedHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.queries.PagedQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toSet;

/**
 * Loads some other products that are related to the loaded product in the controller.
 */
public final class ProductSuggestionsComponent implements ControllerComponent, ProductProjectionLoadedHook, PageDataHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSuggestionsComponent.class);

    private final SphereClient sphereClient;
    private final PriceSelection priceSelection;
    private final int numRecommendations;

    private CompletionStage<PagedQueryResult<ProductProjection>> suggestionsStage;

    @Inject
    ProductSuggestionsComponent(final Configuration configuration, final SphereClient sphereClient,
                                final PriceSelection priceSelection) {
        this.sphereClient = sphereClient;
        this.priceSelection = priceSelection;
        this.numRecommendations = configuration.getInt("productSuggestions.count", 4);
    }

    @Override
    public void onProductProjectionLoaded(final ProductProjection product) {
        this.suggestionsStage = fetchRelatedProducts(product);
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
     * Gets products from the same categories as the given product, excluding the product itself.
     * @param product the product to get suggestions for
     * @return the products related to this product
     */
    private CompletionStage<PagedQueryResult<ProductProjection>> fetchRelatedProducts(final ProductProjection product) {
        final Set<String> categoryIds = product.getCategories().stream()
                .map(Reference::getId)
                .collect(toSet());
        if (categoryIds.isEmpty()) {
            return completedFuture(PagedQueryResult.empty());
        } else {
            return sphereClient.execute(ProductProjectionQuery.ofCurrent()
                    .plusPredicates(m -> m.categories().id().isIn(categoryIds))
                    .plusPredicates(m -> m.id().isNot(product.getId()))
                    .withPriceSelection(priceSelection)
                    .withLimit(numRecommendations));
        }
    }
}
