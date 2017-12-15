package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.viewmodels.ViewModel;

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
