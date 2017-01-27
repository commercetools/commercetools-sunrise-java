package com.commercetools.sunrise.shoppingcart.checkout;

import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class CheckoutWithUpdatedCartPageData extends CheckoutPageData {

    protected CheckoutWithUpdatedCartPageData(final Form<?> form, final Cart cart, @Nullable final Cart updatedCart) {
        super(form, Optional.ofNullable(updatedCart).orElse(cart));
    }
}
