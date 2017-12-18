package com.commercetools.sunrise.shoppingcart.content.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import io.sphere.sdk.carts.Cart;

public class CartPageContent extends PageContent {

    private Cart cart;

    public CartPageContent() {
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(final Cart cart) {
        this.cart = cart;
    }
}
