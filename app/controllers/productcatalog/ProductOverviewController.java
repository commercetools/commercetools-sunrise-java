package controllers.productcatalog;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.models.products.ProductListFetcher;
import com.commercetools.sunrise.productcatalog.productoverview.SunriseProductOverviewController;
import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.ProductFacetedSearchSelectorControllerComponent;
import com.commercetools.sunrise.productcatalog.productoverview.search.pagination.ProductPaginationControllerComponent;
import com.commercetools.sunrise.productcatalog.productoverview.search.searchbox.ProductSearchBoxControllerComponent;
import com.commercetools.sunrise.productcatalog.productoverview.search.sort.ProductSearchSortSelectorControllerComponent;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductOverviewPageContentFactory;

import javax.inject.Inject;

@LogMetrics
@NoCache
@RegisteredComponents({
        ProductSearchSortSelectorControllerComponent.class,
        ProductPaginationControllerComponent.class,
        ProductSearchBoxControllerComponent.class,
        ProductFacetedSearchSelectorControllerComponent.class
})
public final class ProductOverviewController extends SunriseProductOverviewController {

    @Inject
    public ProductOverviewController(final ContentRenderer contentRenderer,
                                     final ProductListFetcher productListFinder,
                                     final ProductOverviewPageContentFactory pageContentFactory) {
        super(contentRenderer, productListFinder, pageContentFactory);
    }

    @Override
    public String getTemplateName() {
        return "pop";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }
}