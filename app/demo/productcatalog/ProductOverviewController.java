package demo.productcatalog;

import com.commercetools.sunrise.common.search.facetedsearch.FacetedSearchComponent;
import com.commercetools.sunrise.common.search.pagination.PaginationComponent;
import com.commercetools.sunrise.common.search.searchbox.SearchBoxComponent;
import com.commercetools.sunrise.common.search.sort.SortSelectorComponent;
import com.commercetools.sunrise.productcatalog.productoverview.SunriseProductOverviewController;

import javax.inject.Inject;

public class ProductOverviewController extends SunriseProductOverviewController {

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