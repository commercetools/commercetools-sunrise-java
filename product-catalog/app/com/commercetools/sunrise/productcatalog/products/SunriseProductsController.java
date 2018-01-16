package com.commercetools.sunrise.productcatalog.products;

import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.categories.CategoryFetcher;
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
    private final CategoryFetcher categoryFetcher;

    protected SunriseProductsController(final TemplateEngine templateEngine,
                                        final ProductListFetcher productListFetcher,
                                        final ProductFetcher productFetcher,
                                        final CategoryFetcher categoryFetcher) {
        this.templateEngine = templateEngine;
        this.productListFetcher = productListFetcher;
        this.productFetcher = productFetcher;
        this.categoryFetcher = categoryFetcher;
    }

    @EnableHooks
    public CompletionStage<Result> list(final String categoryIdentifier) {
        return categoryFetcher.get(categoryIdentifier)
                .thenApply(categoryOpt -> categoryOpt
                        .map(category -> PageData.of().put("category", category))
                        .orElseGet(PageData::of))
                .thenComposeAsync(this::list, HttpExecution.defaultContext());
    }

    @EnableHooks
    public CompletionStage<Result> search() {
        return list(PageData.of());
    }

    private CompletionStage<Result> list(final PageData pageData) {
        return productListFetcher.get()
                .thenApply(products -> pageData.put("products", products))
                .thenComposeAsync(finalPageData -> templateEngine.render("pop", finalPageData)
                        .thenApply(Results::ok), HttpExecution.defaultContext());
    }

    @EnableHooks
    public CompletionStage<Result> show(final String productIdentifier, final String variantIdentifier) {
        return productFetcher.require(productIdentifier, variantIdentifier)
                .thenApply(productWithVariant -> PageData.of()
                        .put("product", productWithVariant.getProduct())
                        .put("variant", productWithVariant.getVariant()))
                .thenComposeAsync(pageData -> templateEngine.render("pdp", pageData)
                        .thenApply(Results::ok), HttpExecution.defaultContext());
    }
}