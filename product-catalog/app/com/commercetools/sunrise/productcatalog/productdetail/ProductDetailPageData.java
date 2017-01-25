package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.common.models.ProductWithVariant;
import io.sphere.sdk.models.Base;

public final class ProductDetailPageData extends Base {

    public final ProductWithVariant productWithVariant;

    public ProductDetailPageData(final ProductWithVariant productWithVariant) {
        this.productWithVariant = productWithVariant;
    }
}
