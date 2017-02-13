package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.search.facetedsearch.FacetedSearchComponent;
import com.commercetools.sunrise.common.search.pagination.PaginationComponent;
import com.commercetools.sunrise.common.search.searchbox.SearchBoxComponent;
import com.commercetools.sunrise.common.search.sort.SortSelectorComponent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.hooks.events.CategoryLoadedHook;
import com.commercetools.sunrise.hooks.events.ProductProjectionPagedSearchResultLoadedHook;
import com.commercetools.sunrise.hooks.events.RequestStartedHook;
import com.commercetools.sunrise.hooks.requests.ProductProjectionSearchHook;
import com.commercetools.sunrise.productcatalog.productoverview.view.ProductOverviewPageContentFactory;
import io.sphere.sdk.categories.Category;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;

/**
 * Provides facilities to search and display products.
 *
 * <p>Components that may be a fit</p>
 * <ul>
 *     <li>{@link SortSelectorComponent}</li>
 *     <li>{@link PaginationComponent}</li>
 *     <li>{@link SearchBoxComponent}</li>
 *     <li>{@link FacetedSearchComponent}</li>
 * </ul>
 * <p id="hooks">supported hooks</p>
 * <ul>
 *     <li>{@link RequestStartedHook}</li>
 *     <li>{@link PageDataReadyHook}</li>
 *     <li>{@link ProductProjectionSearchHook}</li>
 *     <li>{@link CategoryLoadedHook}</li>
 *     <li>{@link ProductProjectionPagedSearchResultLoadedHook}</li>
 * </ul>
 * <p>tags</p>
 * <ul>
 *     <li>product-overview</li>
 *     <li>product-catalog</li>
 *     <li>search</li>
 *     <li>product</li>
 *     <li>category</li>
 * </ul>
 */
@IntroducingMultiControllerComponents(ProductOverviewThemeLinksControllerComponent.class)
public abstract class SunriseProductOverviewController extends SunriseFrameworkController implements WithQueryFlow<ProductsWithCategory> {

    private final CategoryFinder categoryFinder;
    private final ProductListFinder productListFinder;
    private final ProductOverviewPageContentFactory productOverviewPageContentFactory;

    protected SunriseProductOverviewController(final TemplateRenderer templateRenderer, final RequestHookContext hookContext,
                                               final CategoryFinder categoryFinder, final ProductListFinder productListFinder,
                                               final ProductOverviewPageContentFactory productOverviewPageContentFactory) {
        super(templateRenderer, hookContext);
        this.categoryFinder = categoryFinder;
        this.productListFinder = productListFinder;
        this.productOverviewPageContentFactory = productOverviewPageContentFactory;
    }

    @Override
    public String getTemplateName() {
        return "pop";
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("product-overview", "product-catalog", "search", "product", "category"));
    }

    @SunriseRoute("productOverviewPageCall")
    public CompletionStage<Result> searchProductsByCategorySlug(final String languageTag, final String categorySlug) {
        return doRequest(() -> requireProductsWithCategory(categorySlug, this::showPage));
    }

    @SunriseRoute("processSearchProductsForm")
    public CompletionStage<Result> searchProductsBySearchTerm(final String languageTag) {
        return doRequest(() -> requireProducts(null, this::showPage));
    }

    protected final CompletionStage<Result> requireProducts(@Nullable final Category category, final Function<ProductsWithCategory, CompletionStage<Result>> nextAction) {
        return productListFinder.apply(category)
                .thenApply(products -> ProductsWithCategory.of(products, category))
                .thenComposeAsync(nextAction, HttpExecution.defaultContext());
    }

    protected final CompletionStage<Result> requireProductsWithCategory(final String categoryIdentifier, final Function<ProductsWithCategory, CompletionStage<Result>> nextAction) {
        return categoryFinder.apply(categoryIdentifier)
                .thenComposeAsync(categoryOpt -> categoryOpt
                                .map(category -> requireProducts(category, nextAction))
                                .orElseGet(this::handleNotFoundCategory),
                        HttpExecution.defaultContext());
    }

    @Override
    public PageContent createPageContent(final ProductsWithCategory productsWithCategory) {
        return productOverviewPageContentFactory.create(productsWithCategory);
    }

    protected abstract CompletionStage<Result> handleNotFoundCategory();
}