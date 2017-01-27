package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.shoppingcart.checkout.CheckoutWithUpdatedCartPageData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.data.Form;

import javax.annotation.Nullable;
import java.util.List;

public class CheckoutShippingPageData extends CheckoutWithUpdatedCartPageData {

    public final List<ShippingMethod> shippingMethods;

    public CheckoutShippingPageData(final Form<? extends CheckoutShippingFormData> form, final Cart cart,
                                    @Nullable final Cart updatedCart, final List<ShippingMethod> shippingMethods) {
        super(form, cart, updatedCart);
        this.shippingMethods = shippingMethods;
    }
}
