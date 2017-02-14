package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.common.controllers.SunriseTemplateController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.productcatalog.productdetail.view.ProductDetailPageContentFactory;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

/**
 * Controller to show the information about a single product.
 * Loads a {@link ProductProjection} and the selected {@link ProductVariant}.
 */
public abstract class SunriseProductDetailController extends SunriseTemplateController implements WithQueryFlow<ProductWithVariant>, WithRequiredProduct, WithRequiredProductVariant {

    private final ProductFinder productFinder;
    private final ProductVariantFinder productVariantFinder;
    private final ProductDetailPageContentFactory productDetailPageContentFactory;

    protected SunriseProductDetailController(final RequestHookContext hookContext, final TemplateRenderer templateRenderer,
                                             final ProductFinder productFinder, final ProductVariantFinder productVariantFinder,
                                             final ProductDetailPageContentFactory productDetailPageContentFactory) {
        super(hookContext, templateRenderer);
        this.productFinder = productFinder;
        this.productVariantFinder = productVariantFinder;
        this.productDetailPageContentFactory = productDetailPageContentFactory;
    }

    @Override
    public String getTemplateName() {
        return "pdp";
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("product-detail", "product", "product-catalog"));
    }

    @Override
    public ProductFinder getProductFinder() {
        return productFinder;
    }

    @Override
    public ProductVariantFinder getProductVariantFinder() {
        return productVariantFinder;
    }

    @SunriseRoute("productDetailPageCall")
    public CompletionStage<Result> show(final String languageTag, final String productIdentifier, final String variantIdentifier) {
        return doRequest(() ->
                requireProduct(productIdentifier, product ->
                        requireProductVariant(product, variantIdentifier, variant ->
                                showPage(ProductWithVariant.of(product, variant)))));
    }

    @Override
    public PageContent createPageContent(final ProductWithVariant productWithVariant) {
        return productDetailPageContentFactory.create(productWithVariant);
    }
}