package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.controllers.SunriseTemplateController;
import com.commercetools.sunrise.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.ProductReverseRouter;
import com.commercetools.sunrise.productcatalog.productdetail.view.ProductDetailPageContentFactory;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Controller to show the information about a single product.
 * Loads a {@link ProductProjection} and the selected {@link ProductVariant}.
 */
public abstract class SunriseProductDetailController extends SunriseTemplateController implements WithQueryFlow<ProductWithVariant>, WithRequiredProduct, WithRequiredProductVariant {

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
    public ProductFinder getProductFinder() {
        return productFinder;
    }

    @Override
    public ProductVariantFinder getProductVariantFinder() {
        return productVariantFinder;
    }

    @RunRequestStartedHook
    @SunriseRoute(ProductReverseRouter.PRODUCT_DETAIL_PAGE)
    public CompletionStage<Result> show(final String languageTag, final String productIdentifier, final String variantIdentifier) {
        return requireProduct(productIdentifier, product ->
                requireProductVariant(product, variantIdentifier, variant ->
                        showPage(ProductWithVariant.of(product, variant))));
    }

    @Override
    public PageContent createPageContent(final ProductWithVariant productWithVariant) {
        return productDetailPageContentFactory.create(productWithVariant);
    }
}