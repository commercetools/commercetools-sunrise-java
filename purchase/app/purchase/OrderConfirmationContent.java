package purchase;

import common.contexts.UserContext;
import common.controllers.PageContent;
import common.models.ProductDataConfig;
import io.sphere.sdk.orders.Order;

public class OrderConfirmationContent extends PageContent {
    private CartOrderBean order;

    public OrderConfirmationContent() {
    }

    public OrderConfirmationContent(final Order order, final UserContext userContext, final ProductDataConfig productDataConfig) {
        setOrder(new CartOrderBean(order, userContext, productDataConfig));
    }

    @Override
    public String getAdditionalTitle() {
        return null;
    }

    public CartOrderBean getOrder() {
        return order;
    }

    public void setOrder(final CartOrderBean order) {
        this.order = order;
    }
}
