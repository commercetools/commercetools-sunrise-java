package com.commercetools.sunrise.framework.viewmodels.content.products;

import com.commercetools.sunrise.framework.SunriseModel;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;

public final class AttributeWithProductType extends SunriseModel {

    private final Attribute attribute;
    private final Referenceable<ProductType> productTypeRef;

    private AttributeWithProductType(final Attribute attribute, final Referenceable<ProductType> productTypeRef) {
        this.attribute = attribute;
        this.productTypeRef = productTypeRef;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Referenceable<ProductType> getProductTypeRef() {
        return productTypeRef;
    }

    public static AttributeWithProductType of(final Attribute attribute, final Referenceable<ProductType> productTypeRef) {
        return new AttributeWithProductType(attribute, productTypeRef);
    }
}
