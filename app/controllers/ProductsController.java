package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.components.EnableComponents;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.models.BreadcrumbComponent;
import com.commercetools.sunrise.models.categories.CategoryFetcher;
import com.commercetools.sunrise.models.products.ProductFetcher;
import com.commercetools.sunrise.models.products.ProductListFetcher;
import com.commercetools.sunrise.productcatalog.products.ProductSuggestionsComponent;
import com.commercetools.sunrise.productcatalog.products.SunriseProductsController;
import com.commercetools.sunrise.productcatalog.products.search.facetedsearch.ProductFacetedSearchSelectorControllerComponent;
import com.commercetools.sunrise.productcatalog.products.search.pagination.ProductPaginationControllerComponent;
import com.commercetools.sunrise.productcatalog.products.search.searchbox.ProductSearchBoxControllerComponent;
import com.commercetools.sunrise.productcatalog.products.search.sort.ProductSearchSortSelectorControllerComponent;

import javax.inject.Inject;

@LogMetrics
@NoCache
@EnableComponents({
        ProductSuggestionsComponent.class,
        ProductSearchSortSelectorControllerComponent.class,
        ProductPaginationControllerComponent.class,
        ProductSearchBoxControllerComponent.class,
        ProductFacetedSearchSelectorControllerComponent.class,
        BreadcrumbComponent.class
})
public final class ProductsController extends SunriseProductsController {

    @Inject
    ProductsController(final TemplateEngine templateEngine,
                       final ProductListFetcher productListFetcher,
                       final ProductFetcher productFetcher,
                       final CategoryFetcher categoryFetcher) {
        super(templateEngine, productListFetcher, productFetcher, categoryFetcher);
    }
}