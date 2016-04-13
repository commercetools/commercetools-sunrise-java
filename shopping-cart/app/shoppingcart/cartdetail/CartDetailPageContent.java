package shoppingcart.cartdetail;

import common.contexts.UserContext;
import common.controllers.PageContent;
import common.controllers.ReverseRouter;
import common.i18n.I18nIdentifier;
import common.i18n.I18nResolver;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import shoppingcart.common.CartOrderBean;

public class CartDetailPageContent extends PageContent {
    private CartOrderBean cart;

    public CartDetailPageContent() {
    }

    public CartDetailPageContent(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig,
                                 final I18nResolver i18nResolver, final ReverseRouter reverseRouter) {
        this.cart = new CartOrderBean(cart, userContext, productDataConfig, reverseRouter);
        setAdditionalTitle(i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:cartDetailPage.title")));
    }

    public CartOrderBean getCart() {
        return cart;
    }

    public void setCart(final CartOrderBean cart) {
        this.cart = cart;
    }
}
