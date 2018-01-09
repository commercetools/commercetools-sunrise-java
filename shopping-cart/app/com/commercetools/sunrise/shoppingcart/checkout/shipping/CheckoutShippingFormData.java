package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.updateactions.SetShippingMethod;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import java.util.List;

import static java.util.Collections.singletonList;

@ImplementedBy(DefaultCheckoutShippingFormData.class)
public interface CheckoutShippingFormData {

    default List<UpdateAction<Cart>> updateActions() {
        return singletonList(SetShippingMethod.of(ShippingMethod.referenceOfId(shippingMethod())));
    }

    String shippingMethod();

    void applyShippingMethod(final String shippingMethod);
}
