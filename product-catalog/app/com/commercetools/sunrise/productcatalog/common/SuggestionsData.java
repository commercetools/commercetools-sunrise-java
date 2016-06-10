package com.commercetools.sunrise.productcatalog.common;

import io.sphere.sdk.models.Base;

import java.util.List;

public class SuggestionsData extends Base {
    private List<ProductThumbnailBean> list;

    public SuggestionsData() {
    }

    public SuggestionsData(final ProductListBean productListData) {
        this.list = productListData.getList();
    }

    public List<ProductThumbnailBean> getList() {
        return list;
    }

    public void setList(final List<ProductThumbnailBean> list) {
        this.list = list;
    }
}
