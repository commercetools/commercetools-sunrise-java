package productcatalog.productoverview;

import common.inject.RequestScoped;
import productcatalog.productoverview.search.facetedsearch.FacetedSearchComponent;
import productcatalog.productoverview.search.pagination.PaginationComponent;
import productcatalog.productoverview.search.searchbox.SearchBoxComponent;
import productcatalog.productoverview.search.sort.SortSelectorComponent;

import javax.inject.Inject;

@RequestScoped
public class ProductOverviewPageController extends SunriseProductOverviewPageController {

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
}