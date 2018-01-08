package com.commercetools.sunrise.shoppingcart.adddiscountcode;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.AddDiscountCode;

@ImplementedBy(DefaultAddDiscountCodeFormData.class)
public interface AddDiscountCodeFormData {
    String code();

    default AddDiscountCode addDiscountCode() {
        return AddDiscountCode.of(code());
    }
}
