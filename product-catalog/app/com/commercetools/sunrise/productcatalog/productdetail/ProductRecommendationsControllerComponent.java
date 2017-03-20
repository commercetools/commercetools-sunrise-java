package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.recommendations.ProductRecommender;
import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.ProductProjectionLoadedHook;
import com.commercetools.sunrise.productcatalog.productdetail.viewmodels.ProductDetailPageContent;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductListViewModelFactory;
import io.sphere.sdk.products.ProductProjection;
import play.Configuration;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Loads some other products that are related to the loaded product in the controller.
 */
public final class ProductRecommendationsControllerComponent implements ControllerComponent, ProductProjectionLoadedHook, PageDataReadyHook {

    private final ProductRecommender productRecommender;
    private final ProductListViewModelFactory productListViewModelFactory;
    private final int numRecommendations;

    private List<ProductProjection> recommendedProducts;

    @Inject
    public ProductRecommendationsControllerComponent(final ProductRecommender productRecommender,
                                                     final ProductListViewModelFactory productListViewModelFactory, final Configuration configuration) {
        this.productRecommender = productRecommender;
        this.productListViewModelFactory = productListViewModelFactory;
        this.numRecommendations = configuration.getInt("productSuggestions.count", 4);
    }

    @Override
    public CompletionStage<?> onProductProjectionLoaded(final ProductProjection product) {
        return productRecommender.relatedToProduct(product, numRecommendations)
                .thenAccept(recommendations -> this.recommendedProducts = recommendations);
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        if (recommendedProducts != null && pageData.getContent() instanceof ProductDetailPageContent) {
            final ProductDetailPageContent content = (ProductDetailPageContent) pageData.getContent();
            content.setSuggestions(productListViewModelFactory.create(recommendedProducts));
        }
    }
}
