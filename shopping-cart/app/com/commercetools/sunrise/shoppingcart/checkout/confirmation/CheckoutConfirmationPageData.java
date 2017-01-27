package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.shoppingcart.checkout.CheckoutPageData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.orders.Order;
import play.data.Form;

import javax.annotation.Nullable;

public class CheckoutConfirmationPageData extends CheckoutPageData {

    public CheckoutConfirmationPageData(final Form<? extends CheckoutConfirmationFormData> form, final Cart cart, @Nullable final Order order) {
        super(form, cart);
    }
}
