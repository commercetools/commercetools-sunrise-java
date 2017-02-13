package demo.productcatalog;

import com.commercetools.sunrise.common.search.facetedsearch.FacetedSearchComponent;
import com.commercetools.sunrise.common.search.pagination.PaginationComponent;
import com.commercetools.sunrise.common.search.searchbox.SearchBoxComponent;
import com.commercetools.sunrise.common.search.sort.SortSelectorComponent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.productcatalog.productoverview.CategoryFinder;
import com.commercetools.sunrise.productcatalog.productoverview.ProductListFinder;
import com.commercetools.sunrise.productcatalog.productoverview.SunriseProductOverviewController;
import com.commercetools.sunrise.productcatalog.productoverview.view.ProductOverviewPageContentFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class ProductOverviewController extends SunriseProductOverviewController {

    @Inject
    public ProductOverviewController(final TemplateRenderer templateRenderer,
                                     final RequestHookContext hookContext,
                                     final CategoryFinder categoryFinder,
                                     final ProductListFinder productListFinder,
                                     final ProductOverviewPageContentFactory productOverviewPageContentFactory) {
        super(templateRenderer, hookContext, categoryFinder, productListFinder, productOverviewPageContentFactory);
    }

    @Inject
    public void setSortSelectorComponent(final SortSelectorComponent component) {
        registerControllerComponent(component);
    }

    @Inject
    public void setPaginationComponent(final PaginationComponent component) {
        registerControllerComponent(component);
    }

    @Inject
    public void setSearchBoxComponent(final SearchBoxComponent component) {
        registerControllerComponent(component);
    }

    @Inject
    public void setFacetedSearchComponent(final FacetedSearchComponent component) {
        registerControllerComponent(component);
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCategory() {
        return completedFuture(notFound());
    }
}