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

    public CheckoutThankYouContent(final Order order, final UserContext userContext, final ProductDataConfig productDataConfig, final Messages messages, final ReverseRouter reverseRouter) {
        setOrder(new CartOrderBean(order, userContext, productDataConfig, reverseRouter));
        setAdditionalTitle(messages.at("checkoutThankyouPageTitle"));
    }

    public CartOrderBean getOrder() {
        return order;
    }

    public void setOrder(final CartOrderBean order) {
        this.order = order;
    }
}
