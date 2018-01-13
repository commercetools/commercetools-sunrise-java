package com.commercetools.sunrise.productcatalog.products;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ProductProjectionLoadedHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.recommendations.ProductRecommender;
import io.sphere.sdk.products.ProductProjection;
import play.Configuration;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Loads some other products that are related to the loaded product in the controller.
 */
public final class ProductRecommendationsControllerComponent implements ControllerComponent, ProductProjectionLoadedHook, PageDataHook {

    private final ProductRecommender productRecommender;
    private final int numRecommendations;

    private CompletionStage<List<ProductProjection>> recommendedProductsStage;

    @Inject
    public ProductRecommendationsControllerComponent(final ProductRecommender productRecommender,
                                                     final Configuration configuration) {
        this.productRecommender = productRecommender;
        this.numRecommendations = configuration.getInt("productSuggestions.count", 4);
    }

    @Override
    public CompletionStage<?> onProductProjectionLoaded(final ProductProjection product) {
        this.recommendedProductsStage = productRecommender.relatedToProduct(product, numRecommendations);
        return completedFuture(null);
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        if (recommendedProductsStage != null) {
            return recommendedProductsStage.thenApply(products -> pageData.put("recommendedProducts", products));
        }
        return completedFuture(pageData);
    }
}
