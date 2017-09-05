package com.commercetools.sunrise.framework.viewmodels.content.products;

import com.commercetools.sunrise.framework.SunriseModel;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

public final class ProductWithVariant extends SunriseModel {

    private final ProductProjection product;
    private final ProductVariant variant;

    private ProductWithVariant(final ProductProjection product, final ProductVariant variant) {
        this.product = product;
        this.variant = variant;
    }

    public ProductProjection getProduct() {
        return product;
    }

    public ProductVariant getVariant() {
        return variant;
    }

    public static ProductWithVariant of(final ProductProjection product, final ProductVariant variant) {
        return new ProductWithVariant(product, variant);
    }
}
