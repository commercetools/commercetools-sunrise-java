package purchase;

import common.contexts.UserContext;
import common.controllers.PageContent;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;

public class CartDetailPageContent extends PageContent {
    private final CartOrderBean cart;

    public CartDetailPageContent(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig) {
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
