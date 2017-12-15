package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithQueryFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductOverviewPageContentFactory;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Provides facilities to search products.
 */
public abstract class SunriseSearchController extends SunriseContentController implements WithQueryFlow<ProductsWithCategory> {

    private final ProductListFinder productListFinder;
    private final ProductOverviewPageContentFactory productOverviewPageContentFactory;

    protected SunriseSearchController(final ContentRenderer contentRenderer,
                                      final ProductListFinder productListFinder,
                                      final ProductOverviewPageContentFactory productOverviewPageContentFactory) {
        super(contentRenderer);
        this.productListFinder = productListFinder;
        this.productOverviewPageContentFactory = productOverviewPageContentFactory;
    }

    @EnableHooks
    @SunriseRoute(ProductReverseRouter.SEARCH_PROCESS)
    public CompletionStage<Result> process() {
        return findProducts(products ->
                showPage(ProductsWithCategory.of(products)));
    }

    protected final CompletionStage<Result> findProducts(final Function<PagedSearchResult<ProductProjection>, CompletionStage<Result>> nextAction) {
        return productListFinder.apply(null)
                .thenComposeAsync(nextAction, HttpExecution.defaultContext());
    }

    @Override
    public PageContent createPageContent(final ProductsWithCategory productsWithCategory) {
        return productOverviewPageContentFactory.create(productsWithCategory);
    }
}