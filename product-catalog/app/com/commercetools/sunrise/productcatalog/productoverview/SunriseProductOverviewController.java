package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithQueryFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.products.ProductListFetcher;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductOverviewPageContentFactory;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Provides facilities display products by category.
 */
public abstract class SunriseProductOverviewController extends SunriseContentController implements WithQueryFlow<PagedSearchResult<ProductProjection>> {

    private final ProductListFetcher productListFetcher;
    private final ProductOverviewPageContentFactory productOverviewPageContentFactory;

    protected SunriseProductOverviewController(final ContentRenderer contentRenderer,
                                               final ProductListFetcher productListFetcher,
                                               final ProductOverviewPageContentFactory productOverviewPageContentFactory) {
        super(contentRenderer);
        this.productListFetcher = productListFetcher;
        this.productOverviewPageContentFactory = productOverviewPageContentFactory;
    }

    protected final ProductListFetcher getProductListFetcher() {
        return productListFetcher;
    }

    @EnableHooks
    @SunriseRoute(ProductReverseRouter.PRODUCT_OVERVIEW_PAGE)
    public CompletionStage<Result> show(final String categoryIdentifier) {
        return show();
    }

    @EnableHooks
    @SunriseRoute(ProductReverseRouter.SEARCH_PROCESS)
    public CompletionStage<Result> show() {
        return productListFetcher.get().thenComposeAsync(this::showPage, HttpExecution.defaultContext());
    }

    @Override
    public PageContent createPageContent(final PagedSearchResult<ProductProjection> products) {
        return productOverviewPageContentFactory.create(null);
    }
}