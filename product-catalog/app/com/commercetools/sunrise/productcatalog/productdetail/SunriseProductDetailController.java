package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.hooks.events.ProductProjectionLoadedHook;
import com.commercetools.sunrise.hooks.events.ProductVariantLoadedHook;
import com.commercetools.sunrise.hooks.events.RequestStartedHook;
import com.commercetools.sunrise.hooks.requests.ProductProjectionSearchHook;
import com.commercetools.sunrise.productcatalog.productdetail.view.ProductDetailPageContentFactory;
import com.commercetools.sunrise.productcatalog.productsuggestions.ProductSuggestionsControllerComponent;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;

/**
 * Controller to show the information about a single product.
 * Loads one {@link ProductProjection} and the selected {@link ProductVariant}.
 *
 * <p>Components that may be a fit</p>
 * <ul>
 *     <li>{@link ProductSuggestionsControllerComponent}</li>
 * </ul>
 * <p id="hooks">supported hooks</p>
 * <ul>
 *     <li>{@link RequestStartedHook}</li>
 *     <li>{@link PageDataReadyHook}</li>
 *     <li>{@link ProductProjectionSearchHook}</li>
 *     <li>{@link ProductProjectionLoadedHook}</li>
 *     <li>{@link ProductVariantLoadedHook}</li>
 * </ul>
 * <p>tags</p>
 * <ul>
 *     <li>product-detail</li>
 *     <li>product</li>
 *     <li>product-catalog</li>
 * </ul>
 */
public abstract class SunriseProductDetailController extends SunriseFrameworkController implements WithQueryFlow<ProductWithVariant> {

    private final ProductFinder productFinder;
    private final ProductVariantFinder productVariantFinder;
    private final ProductDetailPageContentFactory productDetailPageContentFactory;

    protected SunriseProductDetailController(final TemplateRenderer templateRenderer, final RequestHookContext hookContext,
                                             final ProductFinder productFinder, final ProductVariantFinder productVariantFinder,
                                             final ProductDetailPageContentFactory productDetailPageContentFactory) {
        super(templateRenderer, hookContext);
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

    @SunriseRoute("productDetailPageCall")
    public CompletionStage<Result> show(final String languageTag, final String slug, final String sku) {
        return doRequest(() -> requireProductWithVariant(slug, sku, this::showPage));
    }

    protected abstract CompletionStage<Result> handleNotFoundVariant(final ProductProjection product);

    protected abstract CompletionStage<Result> handleNotFoundProduct();

    @Override
    public PageContent createPageContent(final ProductWithVariant productWithVariant) {
        return productDetailPageContentFactory.create(productWithVariant);
    }

    protected final CompletionStage<Result> requireProductWithVariant(final String productIdentifier, final String variantIdentifier,
                                                                      final Function<ProductWithVariant, CompletionStage<Result>> nextAction) {
        return productFinder.apply(productIdentifier)
                .thenComposeAsync(productOpt -> productOpt
                                .map(product -> requireVariant(product, variantIdentifier, nextAction))
                                .orElseGet(this::handleNotFoundProduct),
                        HttpExecution.defaultContext());
    }

    protected final CompletionStage<Result> requireVariant(final ProductProjection product, final String variantIdentifier,
                                                   final Function<ProductWithVariant, CompletionStage<Result>> nextAction) {
        return productVariantFinder.apply(product, variantIdentifier)
                .thenComposeAsync(variantOpt -> variantOpt
                        .map(variant -> ProductWithVariant.of(product, variant))
                        .map(nextAction)
                        .orElseGet(() -> handleNotFoundVariant(product)));
    }
}