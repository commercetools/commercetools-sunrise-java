package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.shoppingcart.checkout.CheckoutPageData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.data.Form;

import java.util.List;

public class CheckoutShippingPageData extends CheckoutPageData {

    public final List<ShippingMethod> shippingMethods;

    public CheckoutShippingPageData(final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        super(form, cart);
        this.shippingMethods = shippingMethods;
    }
}
