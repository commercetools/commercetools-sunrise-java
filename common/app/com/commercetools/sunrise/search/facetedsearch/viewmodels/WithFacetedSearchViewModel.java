package com.commercetools.sunrise.search.facetedsearch.viewmodels;

public interface WithFacetedSearchViewModel {

    FacetSelectorListViewModel getFacets();

    void setFacets(final FacetSelectorListViewModel facets);
}
