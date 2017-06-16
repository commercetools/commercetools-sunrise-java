package com.commercetools.sunrise.framework.viewmodels.content.products;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

import java.util.List;

public class ProductGalleryViewModel extends ViewModel {

    private List<ProductImageViewModel> list;

    public ProductGalleryViewModel() {
    }

    public List<ProductImageViewModel> getList() {
        return list;
    }

    public void setList(final List<ProductImageViewModel> list) {
        this.list = list;
    }
}
