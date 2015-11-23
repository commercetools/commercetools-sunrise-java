package purchase;

import common.contexts.UserContext;
import common.models.ProductDataConfig;
import common.pages.PageContent;
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
    public String additionalTitle() {
        return "TODO ADDITIONAL TITLE";
    }
}
