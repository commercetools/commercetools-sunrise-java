package com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage;

import io.sphere.sdk.models.Base;

import java.util.List;

public final class ProductsPerPageConfig extends Base {

    private final String key;
    private final List<ProductsPerPageOption> options;
    private final int defaultAmount;

    private ProductsPerPageConfig(final String key, final List<ProductsPerPageOption> options, final int defaultAmount) {
        this.key = key;
        this.options = options;
        this.defaultAmount = defaultAmount;
    }

    public String getKey() {
        return key;
    }

    public List<ProductsPerPageOption> getOptions() {
        return options;
    }

    public int getDefaultAmount() {
        return defaultAmount;
    }

    public static ProductsPerPageConfig of(final String key, final List<ProductsPerPageOption> options, final int defaultAmount) {
        return new ProductsPerPageConfig(key, options, defaultAmount);
    }
}
