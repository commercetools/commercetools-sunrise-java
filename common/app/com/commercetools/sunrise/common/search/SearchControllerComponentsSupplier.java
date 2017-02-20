package com.commercetools.sunrise.common.search;

import com.commercetools.sunrise.common.search.facetedsearch.FacetedSearchControllerComponent;
import com.commercetools.sunrise.common.search.pagination.PaginationControllerComponent;
import com.commercetools.sunrise.common.search.searchbox.SearchBoxControllerComponent;
import com.commercetools.sunrise.common.search.sort.SortSelectorControllerComponent;
import com.commercetools.sunrise.framework.components.AbstractControllerComponentSupplier;

import javax.inject.Inject;

public class SearchControllerComponentsSupplier extends AbstractControllerComponentSupplier {

    @Inject
    public SearchControllerComponentsSupplier(final SortSelectorControllerComponent sortSelectorControllerComponent,
                                              final PaginationControllerComponent paginationControllerComponent,
                                              final SearchBoxControllerComponent searchBoxControllerComponent,
                                              final FacetedSearchControllerComponent facetedSearchControllerComponent) {
        add(sortSelectorControllerComponent);
        add(paginationControllerComponent);
        add(searchBoxControllerComponent);
        add(facetedSearchControllerComponent);
    }
}
