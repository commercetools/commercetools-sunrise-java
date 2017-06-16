package com.commercetools.sunrise.framework.viewmodels.content.products;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

import java.util.List;

public class ProductDetailsViewModel extends ViewModel {

    private List<ProductAttributeViewModel> features;

    public ProductDetailsViewModel() {
    }

    public List<ProductAttributeViewModel> getFeatures() {
        return features;
    }

    public void setFeatures(final List<ProductAttributeViewModel> features) {
        this.features = features;
    }
}
