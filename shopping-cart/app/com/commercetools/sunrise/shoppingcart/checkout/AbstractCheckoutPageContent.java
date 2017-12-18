package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import io.sphere.sdk.carts.Cart;

public abstract class AbstractCheckoutPageContent extends PageContent {

    private CheckoutStep stepWidget;
    private Cart cart;

    public CheckoutStep getStepWidget() {
        return stepWidget;
    }

    public void setStepWidget(final CheckoutStep stepWidget) {
        this.stepWidget = stepWidget;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(final Cart cart) {
        this.cart = cart;
    }
}
