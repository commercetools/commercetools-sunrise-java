package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.common.suggestion.ProductRecommendation;
import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.framework.hooks.events.ProductProjectionLoadedHook;
import com.commercetools.sunrise.productcatalog.common.ProductListBean;
import com.commercetools.sunrise.productcatalog.common.ProductListBeanFactory;
import com.commercetools.sunrise.productcatalog.common.SuggestionsBean;
import com.commercetools.sunrise.productcatalog.productdetail.view.ProductDetailPageContent;
import io.sphere.sdk.products.ProductProjection;
import play.Configuration;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.CompletionStage;

/**
 * Loads some other products that are related to the loaded product in the controller.
 */
public final class ProductRecommendationsControllerComponent implements ControllerComponent, ProductProjectionLoadedHook, PageDataReadyHook {

    private final ProductRecommendation productRecommendation;
    private final ProductListBeanFactory productListBeanFactory;
    private final int numRecommendations;

    private Set<ProductProjection> recommendations;

    @Inject
    public ProductRecommendationsControllerComponent(final ProductRecommendation productRecommendation,
                                                     final ProductListBeanFactory productListBeanFactory, final Configuration configuration) {
        this.productRecommendation = productRecommendation;
        this.productListBeanFactory = productListBeanFactory;
        this.numRecommendations = configuration.getInt("productSuggestions.count", 4);
    }

    @Override
    public CompletionStage<?> onProductProjectionLoaded(final ProductProjection product) {
        return productRecommendation.relatedToProduct(product, numRecommendations)
                .thenAccept(recommendations -> this.recommendations = recommendations);
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        if (recommendations != null && pageData.getContent() instanceof ProductDetailPageContent) {
            final ProductDetailPageContent content = (ProductDetailPageContent) pageData.getContent();
            content.setSuggestions(createSuggestions(recommendations));
        }
    }

    private SuggestionsBean createSuggestions(final Set<ProductProjection> suggestions) {
        final ProductListBean productListData = productListBeanFactory.create(suggestions);
        return new SuggestionsBean(productListData);
    }
}
