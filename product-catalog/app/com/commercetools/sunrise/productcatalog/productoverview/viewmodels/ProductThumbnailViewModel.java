package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import com.commercetools.sunrise.productcatalog.productdetail.viewmodels.ProductViewModel;

public class ProductThumbnailViewModel extends ViewModel {

    private ProductViewModel product;
    private boolean sale;
    private boolean _new;

    public ProductThumbnailViewModel() {
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

    public ProductViewModel getProduct() {
        return product;
    }

    public void setProduct(final ProductViewModel product) {
        this.product = product;
    }
}
