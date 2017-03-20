package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateController;
import com.commercetools.sunrise.framework.controllers.WithQueryFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductOverviewPageContentFactory;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Provides facilities display products by category.
 */
public abstract class SunriseProductOverviewController extends SunriseTemplateController implements WithQueryFlow<ProductsWithCategory>, WithRequiredCategory {

    private final CategoryFinder categoryFinder;
    private final ProductListFinder productListFinder;
    private final ProductOverviewPageContentFactory productOverviewPageContentFactory;

    protected SunriseProductOverviewController(final TemplateRenderer templateRenderer,
                                               final CategoryFinder categoryFinder, final ProductListFinder productListFinder,
                                               final ProductOverviewPageContentFactory productOverviewPageContentFactory) {
        super(templateRenderer);
        this.categoryFinder = categoryFinder;
        this.productListFinder = productListFinder;
        this.productOverviewPageContentFactory = productOverviewPageContentFactory;
    }

    @Override
    public final CategoryFinder getCategoryFinder() {
        return categoryFinder;
    }

    @EnableHooks
    @SunriseRoute(ProductReverseRouter.PRODUCT_OVERVIEW_PAGE)
    public CompletionStage<Result> process(final String languageTag, final String categoryIdentifier) {
        return requireCategory(categoryIdentifier, category ->
                findProducts(category, products ->
                        showPage(ProductsWithCategory.of(products, category))));
    }

    protected final CompletionStage<Result> findProducts(final Category category, final Function<PagedSearchResult<ProductProjection>, CompletionStage<Result>> nextAction) {
        return productListFinder.apply(category)
                .thenComposeAsync(nextAction, HttpExecution.defaultContext());
    }

    @Override
    public PageContent createPageContent(final ProductsWithCategory productsWithCategory) {
        return productOverviewPageContentFactory.create(productsWithCategory);
    }
}