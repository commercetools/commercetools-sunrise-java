package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.components.EnableComponents;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.BreadcrumbComponent;
import com.commercetools.sunrise.models.categories.CategoryFetcher;
import com.commercetools.sunrise.models.products.NewProductFlagComponent;
import com.commercetools.sunrise.models.products.ProductAvailabilityComponent;
import com.commercetools.sunrise.models.products.ProductFetcher;
import com.commercetools.sunrise.models.products.ProductListFetcher;
import com.commercetools.sunrise.productcatalog.products.ProductSuggestionsComponent;
import com.commercetools.sunrise.productcatalog.products.search.facetedsearch.ProductFacetedSearchSelectorControllerComponent;
import com.commercetools.sunrise.productcatalog.products.search.pagination.ProductPaginationControllerComponent;
import com.commercetools.sunrise.productcatalog.products.search.searchbox.ProductSearchBoxControllerComponent;
import com.commercetools.sunrise.productcatalog.products.search.sort.ProductSearchSortSelectorControllerComponent;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@EnableComponents({
        ProductSuggestionsComponent.class,
        ProductSearchSortSelectorControllerComponent.class,
        ProductPaginationControllerComponent.class,
        ProductSearchBoxControllerComponent.class,
        ProductFacetedSearchSelectorControllerComponent.class,
        BreadcrumbComponent.class,
        NewProductFlagComponent.class,
        ProductAvailabilityComponent.class
})
public class ProductsController extends SunriseController {

    private static final String POP_TEMPLATE = "pop";
    private static final String PDP_TEMPLATE = "pdp";

    private final TemplateEngine templateEngine;
    private final ProductListFetcher productListFetcher;
    private final ProductFetcher productFetcher;
    private final CategoryFetcher categoryFetcher;

    @Inject
    ProductsController(final TemplateEngine templateEngine,
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

    @EnableHooks
    public CompletionStage<Result> show(final String productIdentifier, final String variantIdentifier) {
        return productFetcher.require(productIdentifier, variantIdentifier)
                .thenApply(productWithVariant -> PageData.of()
                        .put("product", productWithVariant.getProduct())
                        .put("variant", productWithVariant.getVariant()))
                .thenComposeAsync(pageData -> templateEngine.render(PDP_TEMPLATE, pageData)
                        .thenApply(Results::ok), HttpExecution.defaultContext());
    }

    private CompletionStage<Result> list(final PageData pageData) {
        return productListFetcher.get()
                .thenApply(products -> pageData.put("products", products))
                .thenComposeAsync(finalPageData -> templateEngine.render(POP_TEMPLATE, finalPageData)
                        .thenApply(Results::ok), HttpExecution.defaultContext());
    }
}