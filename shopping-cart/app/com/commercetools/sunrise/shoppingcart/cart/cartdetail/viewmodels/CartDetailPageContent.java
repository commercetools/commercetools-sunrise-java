package com.commercetools.sunrise.shoppingcart.cart.cartdetail.viewmodels;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.models.carts.CartBean;

public class CartDetailPageContent extends PageContent {

    private CartBean cart;

    public CartDetailPageContent() {
    }

    public CartBean getCart() {
        return cart;
    }

    public void setCart(final CartBean cart) {
        this.cart = cart;
    }
}
