package com.commercetools.sunrise.framework.cart.addlineitem;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultAddLineItemFormData.class)
public interface AddLineItemFormData {

    String obtainProductId();

    Integer obtainVariantId();

    Long obtainQuantity();
}
