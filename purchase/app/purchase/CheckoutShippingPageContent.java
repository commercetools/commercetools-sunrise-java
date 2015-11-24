package purchase;

import common.contexts.UserContext;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import play.Configuration;
import play.i18n.Messages;

public class CheckoutShippingPageContent extends CheckoutPageContent {
    private CheckoutShippingFormBean shippingForm;


    public CheckoutShippingPageContent() {
    }

    public CheckoutShippingPageContent(final Cart cart, final Messages messages, final Configuration configuration, final UserContext userContext, final ShippingMethods shippingMethods, final ProductDataConfig productDataConfig) {
        fillDefaults(cart, userContext, productDataConfig, messages);
        setShippingForm(new CheckoutShippingFormBean(cart, userContext, shippingMethods, messages, configuration));
    }

    public CheckoutShippingPageContent(final CheckoutShippingFormData filledForm, final Cart cart, final Messages messages, final Configuration configuration, final UserContext userContext, final ShippingMethods shippingMethods, final ProductDataConfig productDataConfig) {
        fillDefaults(cart, userContext, productDataConfig, messages);
        setShippingForm(new CheckoutShippingFormBean(filledForm, userContext, shippingMethods, messages, configuration));
    }

    private void fillDefaults(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig, final Messages messages) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setShippingStepActive(true);
        setStepWidget(stepWidget);
        setCart(new CartOrderBean(cart, userContext, productDataConfig));
        setAdditionalTitle(messages.at("checkoutShippingPageTitle"));
    }

    public CheckoutShippingFormBean getShippingForm() {
        return shippingForm;
    }

    public void setShippingForm(final CheckoutShippingFormBean shippingForm) {
        this.shippingForm = shippingForm;
    }
}
