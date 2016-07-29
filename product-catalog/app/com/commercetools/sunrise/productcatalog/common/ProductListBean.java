package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.models.ModelBean;

import java.util.List;

public class ProductListBean extends ModelBean {

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
