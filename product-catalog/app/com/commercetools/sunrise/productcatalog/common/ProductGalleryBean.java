package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.models.ModelBean;

import java.util.List;

public class ProductGalleryBean extends ModelBean {

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
