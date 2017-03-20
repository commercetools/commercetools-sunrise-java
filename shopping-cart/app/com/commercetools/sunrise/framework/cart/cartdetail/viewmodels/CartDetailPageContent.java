package com.commercetools.sunrise.framework.cart.cartdetail.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.carts.CartViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;

public class CartDetailPageContent extends PageContent {

    private CartViewModel cart;

    public CartDetailPageContent() {
    }

    public CartViewModel getCart() {
        return cart;
    }

    public void setCart(final CartViewModel cart) {
        this.cart = cart;
    }
}
