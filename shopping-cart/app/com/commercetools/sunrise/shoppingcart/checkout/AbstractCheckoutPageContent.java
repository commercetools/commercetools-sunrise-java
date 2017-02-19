package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.models.carts.CartBean;

public abstract class AbstractCheckoutPageContent extends PageContent {

    private CheckoutStep stepWidget;
    private CartBean cart;

    public CheckoutStep getStepWidget() {
        return stepWidget;
    }

    public void setStepWidget(final CheckoutStep stepWidget) {
        this.stepWidget = stepWidget;
    }

    public CartBean getCart() {
        return cart;
    }

    public void setCart(final CartBean cart) {
        this.cart = cart;
    }
}
