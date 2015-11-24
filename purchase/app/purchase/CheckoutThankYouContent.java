package purchase;

import common.PageContentBean;
import common.contexts.UserContext;
import common.models.ProductDataConfig;
import io.sphere.sdk.orders.Order;
import play.i18n.Messages;

public class CheckoutThankYouContent extends PageContentBean {
    private CartOrderBean order;

    public CheckoutThankYouContent() {
    }

    public CheckoutThankYouContent(final Order order, final UserContext userContext, final ProductDataConfig productDataConfig, final Messages messages) {
        setOrder(new CartOrderBean(order, userContext, productDataConfig));
        setAdditionalTitle(messages.at("checkoutThankyouPageTitle"));
    }

    public CartOrderBean getOrder() {
        return order;
    }

    public void setOrder(final CartOrderBean order) {
        this.order = order;
    }
}
