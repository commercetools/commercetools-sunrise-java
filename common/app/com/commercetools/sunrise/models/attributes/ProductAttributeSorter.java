package com.commercetools.sunrise.models.attributes;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;

import java.util.Comparator;

@ImplementedBy(ProductAttributeSorterImpl.class)
public interface ProductAttributeSorter {

    Comparator<Attribute> compare(String attributeName, Referenceable<ProductType> productTypeRef);
}
