package shoppingcart.checkout.shipping;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.i18n.I18nResolver;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import shoppingcart.common.CartOrderBean;
import shoppingcart.checkout.CheckoutPageContent;
import shoppingcart.checkout.StepWidgetBean;

public class CheckoutShippingPageContent extends CheckoutPageContent {
    private CheckoutShippingFormBean shippingForm;


    public CheckoutShippingPageContent() {
    }

    public CheckoutShippingPageContent(final Cart cart, final ShippingMethods shippingMethods, final ProductDataConfig productDataConfig,
                                       final UserContext userContext, final I18nResolver i18nResolver, final ReverseRouter reverseRouter) {
        fillDefaults(cart, userContext, productDataConfig, i18nResolver, reverseRouter);
        setShippingForm(new CheckoutShippingFormBean(cart, shippingMethods));
    }

    public CheckoutShippingPageContent(final Cart cart, final CheckoutShippingFormData filledForm,
                                       final ShippingMethods shippingMethods, final UserContext userContext,
                                       final ProductDataConfig productDataConfig, final I18nResolver i18nResolver,
                                       final ReverseRouter reverseRouter) {
        fillDefaults(cart, userContext, productDataConfig, i18nResolver, reverseRouter);
        setShippingForm(new CheckoutShippingFormBean(filledForm, shippingMethods));
    }

    private void fillDefaults(final Cart cart, final UserContext userContext, final ProductDataConfig productDataConfig,
                              final I18nResolver i18nResolver, final ReverseRouter reverseRouter) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setShippingStepActive(true);
        setStepWidget(stepWidget);
        setCart(new CartOrderBean(cart, userContext, productDataConfig, reverseRouter));
        setAdditionalTitle(i18nResolver.getOrEmpty(userContext.locales(), "checkout", "shippingPage.title"));
    }

    public CheckoutShippingFormBean getShippingForm() {
        return shippingForm;
    }

    public void setShippingForm(final CheckoutShippingFormBean shippingForm) {
        this.shippingForm = shippingForm;
    }
}
