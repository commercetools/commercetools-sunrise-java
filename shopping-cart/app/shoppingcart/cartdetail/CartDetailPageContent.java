package shoppingcart.cartdetail;

import common.controllers.PageContent;
import shoppingcart.common.CartOrderBean;

public class CartDetailPageContent extends PageContent {

    private CartOrderBean cart;

    public CartDetailPageContent() {
    }

    public CartOrderBean getCart() {
        return cart;
    }

    public void setCart(final CartOrderBean cart) {
        this.cart = cart;
    }
}
