package com.commercetools.sunrise.shoppingcart.removediscountcode;

import com.commercetools.sunrise.framework.SunriseModel;
import play.data.validation.Constraints;

public class DefaultRemoveDiscountCodeFormData extends SunriseModel implements RemoveDiscountCodeFormData {
    @Constraints.Required
    @Constraints.MinLength(1)
    private String discountCodeId;

    @Override
    public String discountCodeId() {
        return discountCodeId;
    }

    public String getDiscountCodeId() {
        return discountCodeId;
    }

    public void setDiscountCodeId(final String discountCodeId) {
        this.discountCodeId = discountCodeId;
    }
}
