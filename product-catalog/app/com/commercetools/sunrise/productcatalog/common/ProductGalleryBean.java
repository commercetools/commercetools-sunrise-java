package com.commercetools.sunrise.productcatalog.common;

import io.sphere.sdk.models.Base;

import java.util.List;

public class ProductGalleryBean extends Base {

    private List<ProductImageBean> list;

    public ProductGalleryBean() {
    }

    public List<ProductImageBean> getList() {
        return list;
    }

    public void setList(final List<ProductImageBean> list) {
        this.list = list;
    }
}
