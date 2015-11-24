package purchase;

import common.PageContentBean;
import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import play.i18n.Messages;

public class CartDetailPageContent extends PageContentBean {
    private CartOrderBean cart;

    public CartDetailPageContent() {
    }

    public CartDetailPageContent(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig, final Messages messages, final ReverseRouter reverseRouter) {
        this.cart = new CartOrderBean(cart, userContext, productDataConfig, reverseRouter);
        setAdditionalTitle(messages.at("cartDetailPageTitle"));
    }

    public CartOrderBean getCart() {
        return cart;
    }

    public void setCart(final CartOrderBean cart) {
        this.cart = cart;
    }
}
