package com.commercetools.sunrise.shoppingcart.checkout;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;
import play.data.Form;

public class CheckoutPageData extends Base {

    public final Form<?> form;
    public final Cart cart;

    public CheckoutPageData(final Form<?> form, final Cart cart) {
        this.form = form;
        this.cart = cart;
    }
}
