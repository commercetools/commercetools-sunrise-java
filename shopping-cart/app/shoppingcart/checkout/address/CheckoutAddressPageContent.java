package shoppingcart.checkout.address;

import common.contexts.ProjectContext;
import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.i18n.I18nResolver;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import play.Configuration;
import shoppingcart.common.CartOrderBean;
import shoppingcart.common.CheckoutPageContent;
import shoppingcart.common.StepWidgetBean;

public class CheckoutAddressPageContent extends CheckoutPageContent {
    private CheckoutAddressFormBean addressForm;

    public CheckoutAddressPageContent() {
    }

    public CheckoutAddressPageContent(final Cart cart, final I18nResolver i18nResolver, final Configuration configuration,
                                      final UserContext userContext, final ProjectContext projectContext,
                                      final ProductDataConfig productDataConfig, final ReverseRouter reverseRouter) {
        fillDefaults(cart, userContext, productDataConfig, i18nResolver, reverseRouter);
        setAddressForm(new CheckoutAddressFormBean(cart, userContext, projectContext, i18nResolver, configuration));
    }

    public CheckoutAddressPageContent(final CheckoutAddressFormData filledForm, final Cart cart, final I18nResolver i18nResolver,
                                      final Configuration configuration, final UserContext userContext, final ProjectContext projectContext,
                                      final ProductDataConfig productDataConfig, final ReverseRouter reverseRouter) {
        fillDefaults(cart, userContext, productDataConfig, i18nResolver, reverseRouter);
        setAddressForm(new CheckoutAddressFormBean(filledForm, userContext, projectContext, i18nResolver, configuration));
    }

    private void fillDefaults(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig,
                              final I18nResolver i18nResolver, final ReverseRouter reverseRouter) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setShippingStepActive(true);
        setStepWidget(stepWidget);
        setCart(new CartOrderBean(cart, userContext, productDataConfig, reverseRouter));
        setAdditionalTitle(i18nResolver.getOrEmpty(userContext.locales(), "checkout", "shippingPage.title"));
    }

    public CheckoutAddressFormBean getAddressForm() {
        return addressForm;
    }

    public void setAddressForm(final CheckoutAddressFormBean addressForm) {
        this.addressForm = addressForm;
    }
}
