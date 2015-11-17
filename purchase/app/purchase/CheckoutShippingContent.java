package purchase;

import common.contexts.UserContext;
import common.models.ProductDataConfig;
import common.pages.PageContent;
import common.pages.ReverseRouter;
import io.sphere.sdk.carts.Cart;
import play.Configuration;
import play.i18n.Messages;
import play.mvc.Http;

public class CheckoutShippingContent extends PageContent {
    private StepWidgetBean stepWidget;
    private CheckoutShippingFormBean shippingForm;
    private CartOrderBean cart;


    public CheckoutShippingContent() {
    }

    public CheckoutShippingContent(final Cart cart, final Messages messages, final Configuration configuration, final ReverseRouter reverseRouter, final UserContext userContext, final Http.Flash flash, final String csrfToken, final ShippingMethods shippingMethods, final ProductDataConfig productDataConfig) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setShippingStepActive(true);
        setStepWidget(stepWidget);
        setShippingForm(new CheckoutShippingFormBean(cart, reverseRouter, csrfToken, userContext, shippingMethods, messages, configuration));
        setCart(new CartOrderBean(cart, userContext, productDataConfig));
    }

    public StepWidgetBean getStepWidget() {
        return stepWidget;
    }

    public void setStepWidget(final StepWidgetBean stepWidget) {
        this.stepWidget = stepWidget;
    }

    public CheckoutShippingFormBean getShippingForm() {
        return shippingForm;
    }

    public void setShippingForm(final CheckoutShippingFormBean shippingForm) {
        this.shippingForm = shippingForm;
    }

    public CartOrderBean getCart() {
        return cart;
    }

    public void setCart(final CartOrderBean cart) {
        this.cart = cart;
    }

    @Override
    public String additionalTitle() {
        return "checkout shipping";
    }
}
