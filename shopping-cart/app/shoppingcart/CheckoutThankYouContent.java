package shoppingcart;

import common.contexts.UserContext;
import common.controllers.PageContent;
import common.controllers.ReverseRouter;
import common.i18n.I18nResolver;
import common.models.ProductDataConfig;
import io.sphere.sdk.orders.Order;

public class CheckoutThankYouContent extends PageContent {
    private CartOrderBean order;

    public CheckoutThankYouContent() {
    }

    public CheckoutThankYouContent(final Order order, final ProductDataConfig productDataConfig, final UserContext userContext,
                                   final ReverseRouter reverseRouter, final I18nResolver i18nResolver) {
        setOrder(new CartOrderBean(order, productDataConfig, userContext, reverseRouter));
        setAdditionalTitle(i18nResolver.getOrEmpty(userContext.locales(), "checkout", "thankYouPage.title"));
    }

    public CartOrderBean getOrder() {
        return order;
    }

    public void setOrder(final CartOrderBean order) {
        this.order = order;
    }
}
