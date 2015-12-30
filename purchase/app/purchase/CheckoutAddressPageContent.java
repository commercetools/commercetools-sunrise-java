package purchase;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import play.Configuration;
import play.i18n.Messages;

public class CheckoutAddressPageContent extends CheckoutPageContent {
    private CheckoutAddressFormBean addressForm;


    public CheckoutAddressPageContent() {
    }

    public CheckoutAddressPageContent(final Cart cart, final Messages messages, final Configuration configuration,
                                      final UserContext userContext, final ProductDataConfig productDataConfig,
                                      final ReverseRouter reverseRouter) {
        fillDefaults(cart, userContext, productDataConfig, messages, reverseRouter);
        setAddressForm(new CheckoutAddressFormBean(cart, userContext, messages, configuration));
    }

    public CheckoutAddressPageContent(final CheckoutAddressFormData filledForm, final Cart cart, final Messages messages,
                                      final Configuration configuration, final UserContext userContext,
                                      final ProductDataConfig productDataConfig, final ReverseRouter reverseRouter) {
        fillDefaults(cart, userContext, productDataConfig, messages, reverseRouter);
        setAddressForm(new CheckoutAddressFormBean(filledForm, userContext, messages, configuration));
    }

    private void fillDefaults(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig,
                              final Messages messages, final ReverseRouter reverseRouter) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setShippingStepActive(true);
        setStepWidget(stepWidget);
        setCart(new CartOrderBean(cart, productDataConfig, userContext, reverseRouter));
        setAdditionalTitle(messages.at("checkoutShippingPageTitle"));
    }

    public CheckoutAddressFormBean getAddressForm() {
        return addressForm;
    }

    public void setAddressForm(final CheckoutAddressFormBean addressForm) {
        this.addressForm = addressForm;
    }
}
