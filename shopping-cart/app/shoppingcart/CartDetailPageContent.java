package shoppingcart;

import common.contexts.UserContext;
import common.controllers.PageContent;
import common.controllers.ReverseRouter;
import common.i18n.I18nResolver;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;

public class CartDetailPageContent extends PageContent {
    private CartOrderBean cart;

    public CartDetailPageContent() {
    }

    public CartDetailPageContent(final Cart cart, final ProductDataConfig productDataConfig, final UserContext userContext,
                                 final ReverseRouter reverseRouter, final I18nResolver i18nResolver) {
        this.cart = new CartOrderBean(cart, productDataConfig, userContext, reverseRouter);
        setAdditionalTitle(i18nResolver.getOrEmpty(userContext.locales(), "checkout", "cartDetailPage.title"));
    }

    public CartOrderBean getCart() {
        return cart;
    }

    public void setCart(final CartOrderBean cart) {
        this.cart = cart;
    }
}
