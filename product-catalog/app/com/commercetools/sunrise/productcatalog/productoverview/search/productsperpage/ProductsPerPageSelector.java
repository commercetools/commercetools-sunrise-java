package com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage;

import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import java.util.List;

public final class ProductsPerPageSelector extends Base {

    private final String key;
    private final List<ProductsPerPageOption> options;
    private final int defaultAmount;
    @Nullable
    private final ProductsPerPageOption selectedOption;

    private ProductsPerPageSelector(final String key, final List<ProductsPerPageOption> options, final int defaultAmount,
                                    @Nullable final ProductsPerPageOption selectedOption) {
        this.key = key;
        this.options = options;
        this.defaultAmount = defaultAmount;
        this.selectedOption = selectedOption;
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

    public int getSelectedPageSize() {
        return selectedOption != null ? selectedOption.getAmount() : defaultAmount;
    }

    public static ProductsPerPageSelector of(final String key, final List<ProductsPerPageOption> options, final int defaultAmount,
                                             @Nullable final ProductsPerPageOption selectedOption) {
        return new ProductsPerPageSelector(key, options, defaultAmount, selectedOption);
    }

}