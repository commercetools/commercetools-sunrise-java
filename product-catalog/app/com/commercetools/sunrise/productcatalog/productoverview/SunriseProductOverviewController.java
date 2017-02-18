package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.controllers.SunriseTemplateController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.productcatalog.productoverview.view.ProductOverviewPageContentFactory;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Provides facilities to search and display products.
 * Loads a {@link io.sphere.sdk.search.PagedSearchResult<io.sphere.sdk.products.ProductProjection>} and an optional {@link Category}.
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
    public CategoryFinder getCategoryFinder() {
        return categoryFinder;
    }

    @RunRequestStartedHook
    @SunriseRoute("productOverviewPageCall")
    public CompletionStage<Result> searchProductsByCategorySlug(final String languageTag, final String categoryIdentifier) {
        return requireCategory(categoryIdentifier, category ->
                findProducts(category, products ->
                        showPage(ProductsWithCategory.of(products, category))));
    }

    @RunRequestStartedHook
    @SunriseRoute("processSearchProductsForm")
    public CompletionStage<Result> searchProductsBySearchTerm(final String languageTag) {
        return findProducts(null, products ->
                showPage(ProductsWithCategory.of(products)));
    }

    protected final CompletionStage<Result> findProducts(@Nullable final Category category, final Function<PagedSearchResult<ProductProjection>, CompletionStage<Result>> nextAction) {
        return productListFinder.apply(category)
                .thenComposeAsync(nextAction, HttpExecution.defaultContext());
    }

    @Override
    public PageContent createPageContent(final ProductsWithCategory productsWithCategory) {
        return productOverviewPageContentFactory.create(productsWithCategory);
    }
}