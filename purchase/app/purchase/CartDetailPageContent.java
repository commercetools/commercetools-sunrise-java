package purchase;

import common.contexts.UserContext;
import common.controllers.PageContent;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import play.i18n.Messages;

public class CartDetailPageContent extends PageContent {
    private CartOrderBean cart;

    public CartDetailPageContent() {
    }

    public CartDetailPageContent(final Cart cart, final ProductDataConfig productDataConfig, final UserContext userContext,
                                 final ReverseRouter reverseRouter, final Messages messages) {
        this.cart = new CartOrderBean(cart, productDataConfig, userContext, reverseRouter);
        setAdditionalTitle(messages.at("cartDetailPageTitle"));
    }

    public CartOrderBean getCart() {
        return cart;
    }

    public void setCart(final CartOrderBean cart) {
        this.cart = cart;
    }
}
