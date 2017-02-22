package com.commercetools.sunrise.framework.cart.cartdetail.viewmodels;

import com.commercetools.sunrise.common.models.carts.CartViewModel;
import com.commercetools.sunrise.common.pages.PageContent;

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
