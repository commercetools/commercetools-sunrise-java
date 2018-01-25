package com.commercetools.sunrise.shoppingcart.checkout;

import io.sphere.sdk.carts.commands.updateactions.SetShippingMethod;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

public class DefaultSetShippingFormData extends Base implements SetShippingFormData {

    @Required
    @MinLength(1)
    public String shippingMethodId;

    @Override
    public SetShippingMethod setShippingMethod() {
        return SetShippingMethod.of(ShippingMethod.referenceOfId(shippingMethodId));
    }
}
