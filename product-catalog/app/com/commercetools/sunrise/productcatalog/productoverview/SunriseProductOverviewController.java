package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithContent;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.products.ProductListFetcher;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Provides facilities display products by category.
 */
public abstract class SunriseProductOverviewController extends SunriseContentController implements WithContent {

    private final ProductListFetcher productListFetcher;

    protected SunriseProductOverviewController(final ContentRenderer contentRenderer,
                                               final ProductListFetcher productListFetcher) {
        super(contentRenderer);
        this.productListFetcher = productListFetcher;
    }

    @EnableHooks
    @SunriseRoute(ProductReverseRouter.PRODUCT_OVERVIEW_PAGE)
    public CompletionStage<Result> show(final String categoryIdentifier) {
        return show();
    }

    @EnableHooks
    @SunriseRoute(ProductReverseRouter.SEARCH_PROCESS)
    public CompletionStage<Result> show() {
        return productListFetcher.get().thenComposeAsync(productList -> {
            final PageData pageData = PageData.of()
                    .putField("products", productList);
            return okResult(pageData);
        }, HttpExecution.defaultContext());
    }
}