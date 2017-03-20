package com.commercetools.sunrise.framework.cart.changelineitemquantity;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultChangeLineItemQuantityFormData.class)
public interface ChangeLineItemQuantityFormData {

    String lineItemId();

    Long quantity();
}
