package shoppingcart.checkout;

import common.controllers.PageContent;
import shoppingcart.common.CartOrderBean;

public abstract class CheckoutPageContent extends PageContent {
    private StepWidgetBean stepWidget;
    private CartOrderBean cart;

    public StepWidgetBean getStepWidget() {
        return stepWidget;
    }

    public void setStepWidget(final StepWidgetBean stepWidget) {
        this.stepWidget = stepWidget;
    }

    public CartOrderBean getCart() {
        return cart;
    }

    public void setCart(final CartOrderBean cart) {
        this.cart = cart;
    }
}
