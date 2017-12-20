package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.core.viewmodels.ViewModel;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

public class ProductListViewModel extends ViewModel {

    private List<ProductProjection> list;

    public ProductListViewModel() {
    }

    public List<ProductProjection> getList() {
        return list;
    }

    public void setList(final List<ProductProjection> list) {
        this.list = list;
    }
}
