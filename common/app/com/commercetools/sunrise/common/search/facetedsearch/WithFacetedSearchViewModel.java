package com.commercetools.sunrise.common.search.facetedsearch;

public interface WithFacetedSearchViewModel {

    FacetSelectorListViewModel getFacets();

    void setFacets(final FacetSelectorListViewModel facets);
}
