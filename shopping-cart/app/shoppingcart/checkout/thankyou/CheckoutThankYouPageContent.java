package shoppingcart.checkout.thankyou;

import common.controllers.PageContent;
import shoppingcart.common.CartOrderBean;

public class CheckoutThankYouPageContent extends PageContent {
    private CartOrderBean order;

    public CheckoutThankYouPageContent() {
    }

    public CartOrderBean getOrder() {
        return order;
    }

    public void setOrder(final CartOrderBean order) {
        this.order = order;
    }
}
