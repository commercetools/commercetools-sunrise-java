package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.models.ModelBean;

public class ProductThumbnailBean extends ModelBean {

    private ProductBean product;
    private boolean sale;
    private boolean _new;

    public ProductThumbnailBean() {
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(final boolean sale) {
        this.sale = sale;
    }

    public boolean isNew() {
        return _new;
    }

    public void setNew(final boolean _new) {
        this._new = _new;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(final ProductBean product) {
        this.product = product;
    }
}
