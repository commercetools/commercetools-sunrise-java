package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.models.products.ProductAttributeViewModel;

import java.util.List;

public class LineItemExtendedViewModel extends LineItemViewModel {

    private List<ProductAttributeViewModel> attributes;

    public LineItemExtendedViewModel() {
    }

    public List<ProductAttributeViewModel> getAttributes() {
        return attributes;
    }

    public void setAttributes(final List<ProductAttributeViewModel> attributes) {
        this.attributes = attributes;
    }
}
