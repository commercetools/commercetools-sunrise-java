package purchase;

import common.contexts.UserContext;
import common.models.ProductDataConfig;
import common.pages.ReverseRouter;
import io.sphere.sdk.carts.Cart;
import play.Configuration;
import play.i18n.Messages;

public class CheckoutShippingPageContent extends CheckoutPageContent {
    private CheckoutShippingFormBean shippingForm;


    public CheckoutShippingPageContent() {
    }

    public CheckoutShippingPageContent(final Cart cart, final Messages messages, final Configuration configuration, final ReverseRouter reverseRouter, final UserContext userContext, final String csrfToken, final ShippingMethods shippingMethods, final ProductDataConfig productDataConfig) {
        fillDefaults(cart, userContext, productDataConfig);
        setShippingForm(new CheckoutShippingFormBean(cart, reverseRouter, csrfToken, userContext, shippingMethods, messages, configuration));
    }

    public CheckoutShippingPageContent(final CheckoutShippingFormData filledForm, final Cart cart, final Messages messages, final Configuration configuration, final ReverseRouter reverseRouter, final UserContext userContext, final String csrfToken, final ShippingMethods shippingMethods, final ProductDataConfig productDataConfig) {
        fillDefaults(cart, userContext, productDataConfig);
        setShippingForm(new CheckoutShippingFormBean(filledForm, reverseRouter, csrfToken, userContext, shippingMethods, messages, configuration));
    }

    private void fillDefaults(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setShippingStepActive(true);
        setStepWidget(stepWidget);
        setCart(new CartOrderBean(cart, userContext, productDataConfig));
    }

    public CheckoutShippingFormBean getShippingForm() {
        return shippingForm;
    }

    public void setShippingForm(final CheckoutShippingFormBean shippingForm) {
        this.shippingForm = shippingForm;
    }

    @Override
    public String additionalTitle() {
        return "checkout shipping";
    }
}
