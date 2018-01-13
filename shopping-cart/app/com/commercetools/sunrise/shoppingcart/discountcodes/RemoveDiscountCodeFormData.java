package com.commercetools.sunrise.shoppingcart.discountcodes;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.RemoveDiscountCode;

@ImplementedBy(DefaultRemoveDiscountCodeFormData.class)
public interface RemoveDiscountCodeFormData {

    RemoveDiscountCode removeDiscountCode();
}
