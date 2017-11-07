package com.commercetools.sunrise.shoppingcart.adddiscountcode;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultAddDiscountCodeFormData.class)
public interface AddDiscountCodeFormData {
    String code();
}
