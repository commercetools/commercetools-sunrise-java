package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.models.ViewModel;

import java.util.List;

public class ProductListBean extends ViewModel {

    private List<ProductThumbnailBean> list;

    public ProductListBean() {
    }

    public List<ProductThumbnailBean> getList() {
        return list;
    }

    public void setList(final List<ProductThumbnailBean> list) {
        this.list = list;
    }
}
