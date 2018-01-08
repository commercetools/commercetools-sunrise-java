package com.commercetools.sunrise.shoppingcart.removediscountcode;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.RemoveDiscountCode;
import io.sphere.sdk.discountcodes.DiscountCode;

@ImplementedBy(DefaultRemoveDiscountCodeFormData.class)
public interface RemoveDiscountCodeFormData {

    default RemoveDiscountCode removeDiscountCode() {
        return RemoveDiscountCode.of(DiscountCode.referenceOfId(discountCodeId()));
    }

    String discountCodeId();
}
