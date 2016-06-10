package shoppingcart.cartdetail;

import com.commercetools.sunrise.common.controllers.PageContent;
import shoppingcart.CartLikeBean;

public class CartDetailPageContent extends PageContent {

    private CartLikeBean cart;

    public CartDetailPageContent() {
    }

    public CartLikeBean getCart() {
        return cart;
    }

    public void setCart(final CartLikeBean cart) {
        this.cart = cart;
    }
}
