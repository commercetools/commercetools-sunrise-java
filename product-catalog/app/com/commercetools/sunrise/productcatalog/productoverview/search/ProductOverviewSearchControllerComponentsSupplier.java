package com.commercetools.sunrise.productcatalog.productoverview.search;

import com.commercetools.sunrise.framework.components.controllers.AbstractControllerComponentSupplier;
import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.ProductFacetedSearchSelectorControllerComponent;
import com.commercetools.sunrise.productcatalog.productoverview.search.pagination.ProductPaginationControllerComponent;
import com.commercetools.sunrise.productcatalog.productoverview.search.searchbox.ProductSearchBoxControllerComponent;
import com.commercetools.sunrise.productcatalog.productoverview.search.sort.ProductSearchSortSelectorControllerComponent;

import javax.inject.Inject;

public class ProductOverviewSearchControllerComponentsSupplier extends AbstractControllerComponentSupplier {

    @Inject
    public ProductOverviewSearchControllerComponentsSupplier(final ProductSearchSortSelectorControllerComponent productSearchSortSelectorControllerComponent,
                                                             final ProductPaginationControllerComponent productSearchPaginationControllerComponent,
                                                             final ProductSearchBoxControllerComponent productSearchBoxControllerComponent,
                                                             final ProductFacetedSearchSelectorControllerComponent productFacetedSearchSelectorControllerComponent) {
        add(productSearchSortSelectorControllerComponent);
        add(productSearchPaginationControllerComponent);
        add(productSearchBoxControllerComponent);
        add(productFacetedSearchSelectorControllerComponent);
    }
}
