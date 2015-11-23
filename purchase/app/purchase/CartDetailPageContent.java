package purchase;

import common.contexts.UserContext;
import common.models.ProductDataConfig;
import common.pages.PageContent;
import common.pages.ReverseRouter;
import io.sphere.sdk.carts.Cart;
import play.i18n.Messages;

public class CartDetailPageContent extends PageContent {
    private final CartOrderBean cart;

    public CartDetailPageContent(final Cart cart, final UserContext userContext, final Messages messages, final ReverseRouter reverseRouter, final ProductDataConfig productDataConfig) {
        this.cart = new CartOrderBean(cart, userContext, productDataConfig);
    }

    public CartOrderBean getCart() {
        return cart;
    }

    @Override
    public String getAdditionalTitle() {
        return "TODO ADDITIONAL TITLE";
    }
}
