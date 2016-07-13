package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch;

import io.sphere.sdk.models.Base;

import java.util.List;

public class FacetSelectorListBean extends Base {

    private List<FacetSelectorBean> list;

    public FacetSelectorListBean() {
    }

    public List<FacetSelectorBean> getList() {
        return list;
    }

    public void setList(final List<FacetSelectorBean> list) {
        this.list = list;
    }
}
