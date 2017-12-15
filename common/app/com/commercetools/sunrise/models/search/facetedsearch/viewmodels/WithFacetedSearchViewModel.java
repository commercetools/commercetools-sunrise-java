package com.commercetools.sunrise.models.search.facetedsearch.viewmodels;

public interface WithFacetedSearchViewModel {

    FacetSelectorListViewModel getFacets();

    void setFacets(final FacetSelectorListViewModel facets);
}
