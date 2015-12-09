package purchase;

import common.contexts.UserContext;
import common.controllers.PageContent;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import io.sphere.sdk.orders.Order;
import play.i18n.Messages;

public class CheckoutThankYouContent extends PageContent {
    private CartOrderBean order;

    public CheckoutThankYouContent() {
    }

    public CheckoutThankYouContent(final Order order, final ProductDataConfig productDataConfig,
                                   final UserContext userContext, final ReverseRouter reverseRouter, final Messages messages) {
        setOrder(new CartOrderBean(order, productDataConfig, userContext, reverseRouter));
        setAdditionalTitle(messages.at("checkoutThankyouPageTitle"));
    }

    public CartOrderBean getOrder() {
        return order;
    }

    public void setOrder(final CartOrderBean order) {
        this.order = order;
    }
}
