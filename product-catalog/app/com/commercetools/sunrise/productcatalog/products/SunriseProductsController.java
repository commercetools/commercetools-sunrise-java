package com.commercetools.sunrise.productcatalog.products;

import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.products.ProductFetcher;
import com.commercetools.sunrise.models.products.ProductListFetcher;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

public abstract class SunriseProductsController extends SunriseController {

    private final TemplateEngine templateEngine;
    private final ProductListFetcher productListFetcher;
    private final ProductFetcher productFetcher;

    protected SunriseProductsController(final TemplateEngine templateEngine,
                                        final ProductListFetcher productListFetcher,
                                        final ProductFetcher productFetcher) {
        this.templateEngine = templateEngine;
        this.productListFetcher = productListFetcher;
        this.productFetcher = productFetcher;
    }

    @EnableHooks
    @SunriseRoute(ProductReverseRouter.PRODUCT_OVERVIEW_PAGE)
    public CompletionStage<Result> list(final String categoryIdentifier) {
        return search();
    }

    @EnableHooks
    @SunriseRoute(ProductReverseRouter.SEARCH_PROCESS)
    public CompletionStage<Result> search() {
        return productListFetcher.get()
                .thenApply(products -> PageData.of().put("products", products))
                .thenComposeAsync(pageData -> templateEngine.render("pop", pageData)
                        .thenApply(Results::ok), HttpExecution.defaultContext());
    }

    @EnableHooks
    @SunriseRoute(ProductReverseRouter.PRODUCT_DETAIL_PAGE)
    public CompletionStage<Result> show(final String productIdentifier, final String variantIdentifier) {
        return productFetcher.require(productIdentifier, variantIdentifier)
                .thenApply(productWithVariant -> PageData.of()
                        .put("product", productWithVariant.getProduct())
                        .put("variant", productWithVariant.getVariant()))
                .thenComposeAsync(pageData -> templateEngine.render("pdp", pageData)
                        .thenApply(Results::ok), HttpExecution.defaultContext());
    }
}