package purchase;

import common.pages.PageContent;

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
