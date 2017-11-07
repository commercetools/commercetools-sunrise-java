package com.commercetools.sunrise.shoppingcart.removediscountcode;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultRemoveDiscountCodeFormData.class)
public interface RemoveDiscountCodeFormData {
    String discountCodeId();
}
