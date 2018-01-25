package com.commercetools.sunrise.shoppingcart.discountcodes;

import io.sphere.sdk.carts.commands.updateactions.RemoveDiscountCode;
import io.sphere.sdk.discountcodes.DiscountCode;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultRemoveDiscountCodeFormData extends Base implements RemoveDiscountCodeFormData {

    @Constraints.Required
    @Constraints.MinLength(1)
    public String discountCodeId;

    @Override
    public RemoveDiscountCode removeDiscountCode() {
        return RemoveDiscountCode.of(DiscountCode.referenceOfId(discountCodeId));
    }
}
