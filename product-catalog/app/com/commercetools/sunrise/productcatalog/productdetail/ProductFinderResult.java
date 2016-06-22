package com.commercetools.sunrise.productcatalog.productdetail;

import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.annotation.Nullable;
import java.util.Optional;

public class ProductFinderResult {

    @Nullable
    private final ProductProjection product;
    @Nullable
    private final ProductVariant variant;

    protected ProductFinderResult(@Nullable final ProductProjection product, @Nullable final ProductVariant variant) {
        this.product = product;
        this.variant = variant;
    }

    public Optional<ProductProjection> getProduct() {
        return Optional.ofNullable(product);
    }

    public Optional<ProductVariant> getVariant() {
        return Optional.ofNullable(variant);
    }

    public static ProductFinderResult of(@Nullable final ProductProjection product, @Nullable final ProductVariant variant) {
        return new ProductFinderResult(product, variant);
    }

    public static ProductFinderResult ofNotFoundVariant(final ProductProjection product) {
        return of(product, null);
    }

    public static ProductFinderResult ofNotFoundProduct() {
        return of(null, null);
    }
}
