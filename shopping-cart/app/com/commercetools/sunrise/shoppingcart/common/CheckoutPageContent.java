package com.commercetools.sunrise.shoppingcart.common;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.shoppingcart.CartBean;

public abstract class CheckoutPageContent extends PageContent {

    private StepWidgetBean stepWidget;
    private CartBean cart;

    public StepWidgetBean getStepWidget() {
        return stepWidget;
    }

    public void setStepWidget(final StepWidgetBean stepWidget) {
        this.stepWidget = stepWidget;
    }

    public CartBean getCart() {
        return cart;
    }

    public void setCart(final CartBean cart) {
        this.cart = cart;
    }
}
