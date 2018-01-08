package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithQueryFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.products.ProductFetcher;
import com.commercetools.sunrise.models.products.ProductWithVariant;
import com.commercetools.sunrise.productcatalog.productdetail.viewmodels.ProductDetailPageContentFactory;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Controller to show the information about a single product.
 * Loads a {@link ProductProjection} and the selected {@link ProductVariant}.
 */
public abstract class SunriseProductDetailController extends SunriseContentController implements WithQueryFlow<ProductWithVariant> {

    private final ProductFetcher productFetcher;
    private final ProductDetailPageContentFactory productDetailPageContentFactory;

    protected SunriseProductDetailController(final ContentRenderer contentRenderer,
                                             final ProductFetcher productFetcher,
                                             final ProductDetailPageContentFactory productDetailPageContentFactory) {
        super(contentRenderer);
        this.productFetcher = productFetcher;
        this.productDetailPageContentFactory = productDetailPageContentFactory;
    }

    protected final ProductFetcher getProductFetcher() {
        return productFetcher;
    }

    @EnableHooks
    @SunriseRoute(ProductReverseRouter.PRODUCT_DETAIL_PAGE)
    public CompletionStage<Result> show(final String productIdentifier, final String productVariantIdentifier) {
        return productFetcher.require(productIdentifier, productVariantIdentifier)
                .thenComposeAsync(this::showPage, HttpExecution.defaultContext());
    }

    @Override
    public PageContent createPageContent(final ProductWithVariant productWithVariant) {
        return productDetailPageContentFactory.create(productWithVariant);
    }
}