package com.commercetools.sunrise.shoppingcart.adddiscountcode;

import com.commercetools.sunrise.framework.SunriseModel;
import play.data.validation.Constraints;

public class DefaultAddDiscountCodeFormData extends SunriseModel implements AddDiscountCodeFormData {
    @Constraints.Required
    @Constraints.MinLength(1)
    private String code;


    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }
}
