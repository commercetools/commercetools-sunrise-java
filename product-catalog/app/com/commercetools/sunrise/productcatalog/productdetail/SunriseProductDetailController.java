package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.framework.controllers.SunriseTemplateController;
import com.commercetools.sunrise.framework.controllers.WithQueryFlow;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.productcatalog.productdetail.viewmodels.ProductDetailPageContentFactory;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Controller to show the information about a single product.
 * Loads a {@link ProductProjection} and the selected {@link ProductVariant}.
 */
public abstract class SunriseProductDetailController extends SunriseTemplateController
        implements WithQueryFlow<ProductWithVariant>, WithRequiredProduct, WithRequiredProductVariant {

    private final ProductFinder productFinder;
    private final ProductVariantFinder productVariantFinder;
    private final ProductDetailPageContentFactory productDetailPageContentFactory;

    protected SunriseProductDetailController(final TemplateRenderer templateRenderer,
                                             final ProductFinder productFinder, final ProductVariantFinder productVariantFinder,
                                             final ProductDetailPageContentFactory productDetailPageContentFactory) {
        super(templateRenderer);
        this.productFinder = productFinder;
        this.productVariantFinder = productVariantFinder;
        this.productDetailPageContentFactory = productDetailPageContentFactory;
    }

    @Override
    public final ProductFinder getProductFinder() {
        return productFinder;
    }

    @Override
    public final ProductVariantFinder getProductVariantFinder() {
        return productVariantFinder;
    }

    @EnableHooks
    @SunriseRoute(ProductReverseRouter.PRODUCT_DETAIL_PAGE)
    public CompletionStage<Result> show(final String languageTag, final String productIdentifier, final String productVariantIdentifier) {
        return requireProduct(productIdentifier, product ->
                requireProductVariant(product, productVariantIdentifier, variant ->
                        showPage(ProductWithVariant.of(product, variant))));
    }

    @Override
    public PageContent createPageContent(final ProductWithVariant productWithVariant) {
        return productDetailPageContentFactory.create(productWithVariant);
    }
}