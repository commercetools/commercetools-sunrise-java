package shoppingcart.checkout.thankyou;

import common.contexts.UserContext;
import common.controllers.PageContent;
import common.controllers.ReverseRouter;
import common.template.i18n.I18nIdentifier;
import common.template.i18n.I18nResolver;
import common.models.ProductDataConfig;
import io.sphere.sdk.orders.Order;
import shoppingcart.common.CartOrderBean;

public class CheckoutThankYouContent extends PageContent {
    private CartOrderBean order;

    public CheckoutThankYouContent() {
    }

    public CheckoutThankYouContent(final Order order, final UserContext userContext, final ProductDataConfig productDataConfig,
                                   final I18nResolver i18nResolver, final ReverseRouter reverseRouter) {
        setOrder(new CartOrderBean(order, userContext, productDataConfig, reverseRouter));
        setAdditionalTitle(i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:thankYouPage.title")));
    }

    public CartOrderBean getOrder() {
        return order;
    }

    public void setOrder(final CartOrderBean order) {
        this.order = order;
    }
}
