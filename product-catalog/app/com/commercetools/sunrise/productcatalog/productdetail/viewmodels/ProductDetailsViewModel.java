package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.models.ViewModel;
import com.commercetools.sunrise.common.models.products.ProductAttributeViewModel;

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
