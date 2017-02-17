package com.commercetools.sunrise.common.search;

import com.commercetools.sunrise.common.search.facetedsearch.FacetedSearchControllerComponent;
import com.commercetools.sunrise.common.search.pagination.PaginationControllerComponent;
import com.commercetools.sunrise.common.search.searchbox.SearchBoxControllerComponent;
import com.commercetools.sunrise.common.search.sort.SortSelectorControllerComponent;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.ControllerComponentListSupplier;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class SearchControllerComponentListSupplier implements ControllerComponentListSupplier {

    private final List<ControllerComponent> components = new ArrayList<>();

    @Inject
    public SearchControllerComponentListSupplier(final SortSelectorControllerComponent sortSelectorControllerComponent,
                                                 final PaginationControllerComponent paginationControllerComponent,
                                                 final SearchBoxControllerComponent searchBoxControllerComponent,
                                                 final FacetedSearchControllerComponent facetedSearchControllerComponent) {
        this.components.addAll(asList(sortSelectorControllerComponent, paginationControllerComponent, searchBoxControllerComponent, facetedSearchControllerComponent));
    }

    @Override
    public List<ControllerComponent> get() {
        return components;
    }
}
