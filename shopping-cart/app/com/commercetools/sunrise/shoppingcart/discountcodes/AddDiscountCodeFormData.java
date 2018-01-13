package com.commercetools.sunrise.shoppingcart.discountcodes;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.AddDiscountCode;

@ImplementedBy(DefaultAddDiscountCodeFormData.class)
public interface AddDiscountCodeFormData {

    AddDiscountCode addDiscountCode();
}
