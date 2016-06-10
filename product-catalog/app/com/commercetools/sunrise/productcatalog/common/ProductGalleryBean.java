package com.commercetools.sunrise.productcatalog.common;

import io.sphere.sdk.models.Base;

import java.util.List;

public class ProductGalleryBean extends Base {

    private List<ProductImageData> list;

    public ProductGalleryBean() {
    }

    public List<ProductImageData> getList() {
        return list;
    }

    public void setList(final List<ProductImageData> list) {
        this.list = list;
    }
}
