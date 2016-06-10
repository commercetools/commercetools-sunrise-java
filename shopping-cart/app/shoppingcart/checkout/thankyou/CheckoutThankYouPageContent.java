package shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.controllers.PageContent;
import shoppingcart.CartLikeBean;

public class CheckoutThankYouPageContent extends PageContent {
    private CartLikeBean order;

    public CheckoutThankYouPageContent() {
    }

    public CartLikeBean getOrder() {
        return order;
    }

    public void setOrder(final CartLikeBean order) {
        this.order = order;
    }
}
