package com.commercetools.sunrise.shoppingcart.discountcodes;

import com.commercetools.sunrise.core.SunriseModel;
import io.sphere.sdk.carts.commands.updateactions.AddDiscountCode;
import play.data.validation.Constraints;

public class DefaultAddDiscountCodeFormData extends SunriseModel implements AddDiscountCodeFormData {

    @Constraints.Required
    @Constraints.MinLength(1)
    public String code;

    @Override
    public AddDiscountCode addDiscountCode() {
        return AddDiscountCode.of(code);
    }
}
