package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateController;
import com.commercetools.sunrise.framework.controllers.WithQueryFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
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
public abstract class SunriseSearchController extends SunriseTemplateController implements WithQueryFlow<ProductsWithCategory> {

    private final ProductListFinder productListFinder;
    private final ProductOverviewPageContentFactory productOverviewPageContentFactory;

    protected SunriseSearchController(final TemplateRenderer templateRenderer,
                                      final ProductListFinder productListFinder,
                                      final ProductOverviewPageContentFactory productOverviewPageContentFactory) {
        super(templateRenderer);
        this.productListFinder = productListFinder;
        this.productOverviewPageContentFactory = productOverviewPageContentFactory;
    }

    @EnableHooks
    @SunriseRoute(ProductReverseRouter.SEARCH_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
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