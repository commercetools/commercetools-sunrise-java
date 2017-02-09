package com.commercetools.sunrise.common.models;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;

public class AttributeWithProductType extends Base {

    private final Attribute attribute;
    private final Referenceable<ProductType> productTypeRef;

    public AttributeWithProductType(final Attribute attribute, final Referenceable<ProductType> productTypeRef) {
        this.attribute = attribute;
        this.productTypeRef = productTypeRef;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Referenceable<ProductType> getProductTypeRef() {
        return productTypeRef;
    }
}
