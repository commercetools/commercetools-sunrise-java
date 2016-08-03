package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.shoppingcart.CartBean;

public class CheckoutThankYouPageContent extends PageContent {
    private CartBean order;

    public CheckoutThankYouPageContent() {
    }

    public CartBean getOrder() {
        return order;
    }

    public void setOrder(final CartBean order) {
        this.order = order;
    }
}
