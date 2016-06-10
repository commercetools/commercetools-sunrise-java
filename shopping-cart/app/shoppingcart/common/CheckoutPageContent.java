package shoppingcart.common;

import com.commercetools.sunrise.common.controllers.PageContent;
import shoppingcart.CartLikeBean;

public abstract class CheckoutPageContent extends PageContent {

    private StepWidgetBean stepWidget;
    private CartLikeBean cart;

    public StepWidgetBean getStepWidget() {
        return stepWidget;
    }

    public void setStepWidget(final StepWidgetBean stepWidget) {
        this.stepWidget = stepWidget;
    }

    public CartLikeBean getCart() {
        return cart;
    }

    public void setCart(final CartLikeBean cart) {
        this.cart = cart;
    }
}
