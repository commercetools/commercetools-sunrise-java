package purchase;

import common.contexts.UserContext;
import common.models.ProductDataConfig;
import common.controllers.PageContent;
import common.controllers.ReverseRouter;
import io.sphere.sdk.carts.Cart;

public class CartDetailPageContent extends PageContent {
    private final CartOrderBean cart;

    public CartDetailPageContent(final Cart cart, final ProductDataConfig productDataConfig,
                                 final UserContext userContext, final ReverseRouter reverseRouter) {
        this.cart = new CartOrderBean(cart, productDataConfig, userContext, reverseRouter);
    }

    public CartOrderBean getCart() {
        return cart;
    }

    @Override
    public String getAdditionalTitle() {
        return "TODO ADDITIONAL TITLE";
    }
}
