package com.commercetools.sunrise.common.models;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

public class ProductWithVariant extends Base {

    private final ProductProjection product;
    private final ProductVariant variant;

    public ProductWithVariant(final ProductProjection product, final ProductVariant variant) {
        this.product = product;
        this.variant = variant;
    }

    public ProductProjection getProduct() {
        return product;
    }

    public ProductVariant getVariant() {
        return variant;
    }
}
