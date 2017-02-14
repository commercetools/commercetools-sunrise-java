package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.controllers.SunriseTemplateController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
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
 * Loads a {@link io.sphere.sdk.search.PagedSearchResult<io.sphere.sdk.products.ProductProjection>} and an optional {@link Category}.
 */
@IntroducingMultiControllerComponents(ProductOverviewThemeLinksControllerComponent.class)
public abstract class SunriseProductOverviewController extends SunriseTemplateController implements WithQueryFlow<ProductsWithCategory>, WithRequiredCategory {

    private final CategoryFinder categoryFinder;
    private final ProductListFinder productListFinder;
    private final ProductOverviewPageContentFactory productOverviewPageContentFactory;

    protected SunriseProductOverviewController(final RequestHookContext hookContext, final TemplateRenderer templateRenderer,
                                               final CategoryFinder categoryFinder, final ProductListFinder productListFinder,
                                               final ProductOverviewPageContentFactory productOverviewPageContentFactory) {
        super(hookContext, templateRenderer);
        this.categoryFinder = categoryFinder;
        this.productListFinder = productListFinder;
        this.productOverviewPageContentFactory = productOverviewPageContentFactory;
    }

    @Override
    public String getTemplateName() {
        return "pop";
    }

    @Override
    public CategoryFinder getCategoryFinder() {
        return categoryFinder;
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("product-overview", "product-catalog", "search", "product", "category"));
    }

    @SunriseRoute("productOverviewPageCall")
    public CompletionStage<Result> searchProductsByCategorySlug(final String languageTag, final String categoryIdentifier) {
        return doRequest(() ->
                requireCategory(categoryIdentifier, category ->
                        findProducts(category, this::showPage)));
    }

    @SunriseRoute("processSearchProductsForm")
    public CompletionStage<Result> searchProductsBySearchTerm(final String languageTag) {
        return doRequest(() -> findProducts(null, this::showPage));
    }

    protected final CompletionStage<Result> findProducts(@Nullable final Category category, final Function<ProductsWithCategory, CompletionStage<Result>> nextAction) {
        return productListFinder.apply(category)
                .thenApply(products -> ProductsWithCategory.of(products, category))
                .thenComposeAsync(nextAction, HttpExecution.defaultContext());
    }

    @Override
    public PageContent createPageContent(final ProductsWithCategory productsWithCategory) {
        return productOverviewPageContentFactory.create(productsWithCategory);
    }
}