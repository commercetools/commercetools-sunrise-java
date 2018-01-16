package com.commercetools.sunrise.models.attributes;

import com.commercetools.sunrise.models.SelectOption;
import com.commercetools.sunrise.models.products.AttributeSelectOption;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.List;

@ImplementedBy(AttributeSelectorImpl.class)
public interface AttributeSelector {

    List<AttributeSelectOption> options(String attributeName, ProductProjection selectedProduct, ProductVariant selectedVariant);
}
