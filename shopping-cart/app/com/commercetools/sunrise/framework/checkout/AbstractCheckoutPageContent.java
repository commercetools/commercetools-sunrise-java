package com.commercetools.sunrise.framework.checkout;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.viewmodels.content.carts.CartViewModel;

public abstract class AbstractCheckoutPageContent extends PageContent {

    private CheckoutStep stepWidget;
    private CartViewModel cart;

    public CheckoutStep getStepWidget() {
        return stepWidget;
    }

    public void setStepWidget(final CheckoutStep stepWidget) {
        this.stepWidget = stepWidget;
    }

    public CartViewModel getCart() {
        return cart;
    }

    public void setCart(final CartViewModel cart) {
        this.cart = cart;
    }
}
