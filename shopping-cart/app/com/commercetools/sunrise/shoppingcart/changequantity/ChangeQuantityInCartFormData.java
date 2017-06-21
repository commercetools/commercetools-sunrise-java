package com.commercetools.sunrise.shoppingcart.changequantity;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultChangeQuantityInCartFormData.class)
public interface ChangeQuantityInCartFormData {

    String lineItemId();

    Long quantity();
}
