package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithFetchFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
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
import play.twirl.api.Content;

import java.util.HashSet;
import java.util.Optional;
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
public abstract class SunriseProductDetailController extends SunriseFrameworkController implements WithTemplateName, WithFetchFlow<ProductWithVariant> {

    private final ProductFinder productFinder;
    private final ProductDetailPageContentFactory productDetailPageContentFactory;

    protected SunriseProductDetailController(final ProductFinder productFinder, final ProductDetailPageContentFactory productDetailPageContentFactory) {
        this.productFinder = productFinder;
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
    public CompletionStage<Content> renderPage(final ProductWithVariant productWithVariant) {
        final PageContent pageContent = productDetailPageContentFactory.create(productWithVariant);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    protected final CompletionStage<Result> requireProductWithVariant(final String productIdentifier, final String variantIdentifier,
                                                                      final Function<ProductWithVariant, CompletionStage<Result>> nextAction) {
        return productFinder.apply(productIdentifier)
                .thenComposeAsync(productOpt -> productOpt
                                .map(product -> findProductVariant(product, variantIdentifier)
                                        .map(variant -> {
                                            runHookOnFoundVariant(product, variant);
                                            return ProductWithVariant.of(product, variant);
                                        })
                                        .map(nextAction)
                                        .orElseGet(() -> handleNotFoundVariant(product)))
                                .orElseGet(this::handleNotFoundProduct),
                        HttpExecution.defaultContext());
    }

    protected final void runHookOnFoundVariant(final ProductProjection product, final ProductVariant variant) {
        ProductVariantLoadedHook.runHook(hooks(), product, variant);
    }

    protected Optional<ProductVariant> findProductVariant(final ProductProjection product, final String variantIdentifier) {
        return product.findVariantBySku(variantIdentifier);
    }
}