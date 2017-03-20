package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

import java.util.List;

public class ProductListViewModel extends ViewModel {

    private List<ProductThumbnailViewModel> list;

    public ProductListViewModel() {
    }

    public List<ProductThumbnailViewModel> getList() {
        return list;
    }

    public void setList(final List<ProductThumbnailViewModel> list) {
        this.list = list;
    }
}
