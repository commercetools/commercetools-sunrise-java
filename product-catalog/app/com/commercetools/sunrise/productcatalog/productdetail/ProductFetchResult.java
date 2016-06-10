package com.commercetools.sunrise.productcatalog.productdetail;

import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.annotation.Nullable;
import java.util.Optional;

public class ProductFetchResult {

    @Nullable
    private final ProductProjection product;
    @Nullable
    private final ProductVariant variant;

    public ProductFetchResult(@Nullable final ProductProjection product, @Nullable final ProductVariant variant) {
        this.product = product;
        this.variant = variant;
    }

    public Optional<ProductProjection> getProduct() {
        return Optional.ofNullable(product);
    }

    public Optional<ProductVariant> getVariant() {
        return Optional.ofNullable(variant);
    }

    public static ProductFetchResult of(final ProductProjection product, final ProductVariant variant) {
        return new ProductFetchResult(product, variant);
    }

    public static ProductFetchResult ofNotFoundVariant(final ProductProjection product) {
        return new ProductFetchResult(product, null);
    }

    public static ProductFetchResult ofNotFoundProduct() {
        return new ProductFetchResult(null, null);
    }
}
