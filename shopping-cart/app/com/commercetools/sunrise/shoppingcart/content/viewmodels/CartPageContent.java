package com.commercetools.sunrise.shoppingcart.content.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.carts.CartViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;

public class CartPageContent extends PageContent {

    private CartViewModel cart;

    public CartPageContent() {
    }

    public CartViewModel getCart() {
        return cart;
    }

    public void setCart(final CartViewModel cart) {
        this.cart = cart;
    }
}
