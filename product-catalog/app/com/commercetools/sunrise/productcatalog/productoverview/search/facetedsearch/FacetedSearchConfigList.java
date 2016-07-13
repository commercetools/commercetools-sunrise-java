package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch;

import io.sphere.sdk.models.Base;

import java.util.List;

public final class FacetedSearchConfigList extends Base {

    private final List<SelectFacetedSearchConfig> selectFacetedSearchConfigList;
    // missing range facets


    private FacetedSearchConfigList(final List<SelectFacetedSearchConfig> selectFacetedSearchConfigList) {
        this.selectFacetedSearchConfigList = selectFacetedSearchConfigList;
    }

    public List<SelectFacetedSearchConfig> getSelectFacetedSearchConfigList() {
        return selectFacetedSearchConfigList;
    }

    public static FacetedSearchConfigList of(final List<SelectFacetedSearchConfig> selectFacetConfigList) {
        return new FacetedSearchConfigList(selectFacetConfigList);
    }
}
