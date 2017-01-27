package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.common.models.ControllerData;
import com.commercetools.sunrise.common.models.ProductWithVariant;

public final class ProductDetailControllerData extends ControllerData {

    private final ProductWithVariant productWithVariant;

    public ProductDetailControllerData(final ProductWithVariant productWithVariant) {
        this.productWithVariant = productWithVariant;
    }

    public ProductWithVariant getProductWithVariant() {
        return productWithVariant;
    }
}
